package com.muse.cloud.operate;

import com.muse.tool.util.type.error.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import redis.clients.jedis.Jedis;

import java.util.Optional;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/4/4 15:27
 * @Since
 */
@Slf4j
public class ResourceRedisMap extends RedisSourceReader {
    private final String key;

    public ResourceRedisMap(GenericObjectPool<Jedis> pool, String key) {
        super(pool, key);
        this.key = key;
    }

    @Override
    public Object get(String name) throws RedisConnectionException {
        Optional<Object> optional = cacheSource(name);
        if (optional == null) {
            Object notCache;
            try {
                Jedis jedis = pool.borrowObject();
                try {
                    refresh(jedis);
                    notCache = jedis.hget(key, name);
                } finally {
                    pool.returnObject(jedis);
                }
            } catch (Exception e) {
                throw new RedisConnectionException(e);
            }
            cachePut(name, Optional.ofNullable(notCache));
            return notCache;
        } else {
            return optional.orElse(null);
        }
    }
}
