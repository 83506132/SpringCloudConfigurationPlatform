package org.muse.redis.config.autoconfigure.profile;

import com.muse.cloud.operate.GenericObjectPoolConfigFactory;
import com.muse.cloud.operate.RedisConnectFactory;
import com.muse.cloud.operate.RedisSourceReader;
import com.muse.cloud.operate.ResourceRedisMap;
import com.muse.tool.util.type.error.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.springframework.boot.env.RandomValuePropertySource;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
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
    private static GenericObjectPool<Jedis> jedisGenericObjectPool;

    public RedisConfigurePropertySource(String name, RedisSourceReader source) {
        super(name, source);
    }

    public static GenericObjectPool<Jedis> getJedisGenericObjectPool() {
        return jedisGenericObjectPool;
    }

    private static GenericObjectPool<Jedis> initialJedisGenericObjectPool(RedisConfigureProfile redisInfo) {
        return jedisGenericObjectPool = new GenericObjectPool<Jedis>(new RedisConnectFactory(redisInfo), GenericObjectPoolConfigFactory.generateDefault());
    }

    public static void addToEnvironment(ConfigurableEnvironment environment, DeferredLogFactory logFactory) throws Exception {
        final RedisConfigureProfile redisInfo = new RedisConfigureProfileFactory(environment).prepareCreate();
        if (redisInfo.isEnable() && !redisInfo.getProfile().isEmpty()) {
            GenericObjectPoolConfigFactory.connectTest(initialJedisGenericObjectPool(redisInfo));
            final MutablePropertySources propertySources = environment.getPropertySources();
            redisInfo.getProfile().stream().forEach(name -> new RedisConfigurePropertySource(String.format("Config resource [%s] via origin '%s'", name, redisInfo.host()), new ResourceRedisMap(getJedisGenericObjectPool(), name)).processAndApply(propertySources));
        }
    }

    static boolean isTheType(Object object) {
        return object instanceof RedisConfigurePropertySource;
    }

    static boolean isTheType(Class<?> klass) {
        return RedisConfigurePropertySource.class == klass;
    }

    private void processAndApply(final MutablePropertySources propertySources) {
        propertySources.addAfter(RandomValuePropertySource.RANDOM_PROPERTY_SOURCE_NAME, this);
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
