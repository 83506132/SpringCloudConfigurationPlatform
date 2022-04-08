package org.muse.redis.config.autoconfigure;

import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/4/3 23:36
 * @Since
 */
public abstract class ProfileInfoFactory<T> {
    protected final String prefix;
    protected final String[] activeProfiles;
    protected final MutablePropertySources propertySources;
    protected final ConfigurableConversionService conversionService;

    public ProfileInfoFactory(ConfigurableEnvironment environment) {
        prefix = environment.resolvePlaceholders("${spring.application.name:application}");
        activeProfiles = environment.getActiveProfiles();
        propertySources = environment.getPropertySources();
        conversionService = environment.getConversionService();
    }


    /**
     * create Property info object
     * @return configure info object
     */
    public abstract T prepareCreate();

}
