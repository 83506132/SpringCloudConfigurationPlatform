package com.muse.cloud.operate;

import com.muse.cloud.operate.profile.RedisConnectInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import redis.clients.jedis.Jedis;

import java.util.Objects;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/4/4 15:29
 * @Since
 */
@Slf4j
public class RedisConnectFactory extends BasePooledObjectFactory<Jedis> {
    private RedisConnectInfo info;

    public RedisConnectFactory(RedisConnectInfo info) {
        this.info = info;
    }

    @Override
    public Jedis create() throws Exception {
        Jedis jedis = new Jedis(info.getIp(), info.getPort());
        if (Objects.equals(info.getUser(), "") && Objects.equals(info.getPassword(), "")) {
            jedis.auth(info.getUser(), info.getPassword());
        } else if (Objects.equals(info.getPassword(), "")) {
            jedis.auth(info.getPassword());
        }
        return jedis;
    }

    @Override
    public PooledObject<Jedis> wrap(Jedis jedis) {
        return new DefaultPooledObject<>(jedis);
    }
}
