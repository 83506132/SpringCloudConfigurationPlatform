package org.muse.redis.config.autoconfigure.profile;

import com.muse.cloud.operate.GenericObjectPoolConfigFactory;
import com.muse.cloud.operate.RedisConnectFactory;
import com.muse.cloud.operate.RedisSourceReader;
import com.muse.cloud.operate.ResourceRedisMap;
import com.muse.tool.util.type.error.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import redis.clients.jedis.Jedis;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/4/3 14:00
 * @Since
 */
@Slf4j
class RedisConfigurePropertySource extends PropertySource<RedisSourceReader> {
    private static final Log logger = LogFactory.getLog(RandomValuePropertySource.class);

    public RedisConfigurePropertySource(String name, RedisSourceReader source) {
        super(name, source);
    }


    public static void addToEnvironment(ConfigurableEnvironment environment, DeferredLogFactory logFactory) throws Exception {
        RedisConfigureProfile redisInfo = new RedisConfigureProfileFactory(environment).prepareCreate();
        if (redisInfo.isEnable() && !redisInfo.getProfile().isEmpty()) {
            final GenericObjectPool<Jedis> jedisGenericObjectPool = new GenericObjectPool<Jedis>(new RedisConnectFactory(redisInfo), GenericObjectPoolConfigFactory.generateDefault());
            Jedis jedis = jedisGenericObjectPool.borrowObject(2000);
            try {
                jedis.connect();
            } finally {
                jedisGenericObjectPool.returnObject(jedis);
            }
            for (String name : redisInfo.getProfile()) {
                environment.getPropertySources().addAfter(RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME, new RedisConfigurePropertySource(String.format("Config resource [%s] via origin '%s'", name, redisInfo.host()), new ResourceRedisMap(jedisGenericObjectPool, name)));
            }
        }
    }

    static boolean isTheType(Object object) {
        return object instanceof RedisConfigurePropertySource;
    }

    static boolean isTheType(Class<?> klass) {
        return RedisConfigurePropertySource.class == klass;
    }

    @Override
    public Object getProperty(String name) {
        Object result = null;
        try {
            result = source.get(name);
        } catch (RedisConnectionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
