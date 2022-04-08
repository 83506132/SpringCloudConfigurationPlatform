package org.muse.redis.config.autoconfigure;

import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.MutablePropertySources;

/**
 * description: load source parse for object
 *
 * @Author ZhaoMuse
 * @date 2022/4/3 15:08
 * @Since 1.0
 */
public abstract class PropertyParseObject {
    /**
     * load in profile list
     */
    protected final MutablePropertySources sources;

    protected final ConfigurableConversionService conversionService;

    /**
     * parse name
     */
    private final String name;

    public PropertyParseObject(MutablePropertySources sources, ConfigurableConversionService conversionService, String name) {
        this.sources = sources;
        this.conversionService = conversionService;
        this.name = name;
    }

    public PropertyParseObject(MutablePropertySources sources, ConfigurableConversionService conversionService) {
        this(sources, conversionService, "defaultDefinition");
    }

    /**
     * pass class parse annotation get source for generate object
     *
     * @param toObject require parse of class object
     * @param <T>      correspond of object type
     * @return object property
     */
    public abstract <T> T getGenerate(Class<T> toObject);
}
