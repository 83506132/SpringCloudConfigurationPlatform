package org.muse.redis.config.autoconfigure.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.logging.DeferredLogFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * description: load configure source EnvironmentPostProcessor
 *
 * @Author ZhaoMuse
 * @date 2022/4/3 10:14
 * @Since
 */
public class EnvironmentAdditionalConfigSource implements EnvironmentPostProcessor, Ordered {

    final DeferredLogFactory logFactory;

    public EnvironmentAdditionalConfigSource(DeferredLogFactory logFactory) {
        this.logFactory = logFactory;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        try {
            RedisConfigurePropertySource.addToEnvironment(environment, logFactory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
