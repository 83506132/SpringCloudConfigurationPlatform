package com.muse.cloud.operate;

import com.muse.tool.util.type.error.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/4/4 15:05
 * @Since
 */
@Slf4j
public abstract class RedisSourceReader {
    protected static final String VERSION = "FileText.Version";
    protected static final String TEXT = "FileText";
    protected final String mapKey;
    protected final Map<String, Object> sourceOrCache = new ConcurrentHashMap();
    protected final AtomicInteger version = new AtomicInteger(0);
    protected GenericObjectPool<Jedis> pool;

    {
        initialSource(sourceOrCache);
    }

    public RedisSourceReader(GenericObjectPool<Jedis> pool, String mapKey) {
        this.pool = pool;
        this.mapKey = mapKey;
    }

    protected Optional<Object> cacheSource(String key) {
        return (Optional<Object>) sourceOrCache.get(key);
    }

    protected void cachePut(String key, Optional<Object> object) {
        sourceOrCache.put(key, object);
    }

    public void initialSource(Map<String, Object> cache) {
    }

    /**
     * enter cache find
     *
     * @param name parameterName
     * @return correspond type parameter
     * @throws RedisConnectionException 处理异常
     */
    public abstract Object get(String name) throws RedisConnectionException;

    public final Jedis refresh(Jedis jedis) {
        int i, version = Integer.parseInt(jedis.hget(mapKey, VERSION));
        while (version > (i = this.version.get())) {
            if (!this.version.compareAndSet(i, version)) {
                sourceOrCache.clear();
                initialSource(sourceOrCache);
                break;
            }
        }
        return jedis;
    }

    public void clear() {
        sourceOrCache.clear();
    }
}
