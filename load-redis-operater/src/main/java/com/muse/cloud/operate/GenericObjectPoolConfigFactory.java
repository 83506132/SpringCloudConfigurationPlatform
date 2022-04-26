package com.muse.cloud.operate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/4/8 19:47
 * @Since
 */
@Slf4j
public class GenericObjectPoolConfigFactory {
    public static GenericObjectPoolConfig generateDefault() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMinIdle(1);
        poolConfig.setMaxIdle(3);
        poolConfig.setMaxTotal(5);
        return poolConfig;
    }

    public static void connectTest(GenericObjectPool<Jedis> pool) throws Exception {
        final Jedis jedis = pool.borrowObject(2000);
        try {
            jedis.connect();
        } finally {
            pool.returnObject(jedis);
        }
    }
}
