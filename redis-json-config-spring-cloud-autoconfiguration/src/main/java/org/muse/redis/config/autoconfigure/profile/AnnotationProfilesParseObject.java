package org.muse.redis.config.autoconfigure.profile;

import com.muse.tool.util.ObjectTools;
import com.muse.tool.util.StringTools;
import com.muse.tool.util.access.ObjectAccess;
import com.muse.tool.util.type.error.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.muse.redis.config.autoconfigure.PropertyParseObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.convert.support.ConfigurableConversionService;
import org.springframework.core.env.MutablePropertySources;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/4/3 16:38
 * @Since
 */
@Slf4j
public class AnnotationProfilesParseObject extends PropertyParseObject {

    public AnnotationProfilesParseObject(MutablePropertySources sources, ConfigurableConversionService conversionService) {
        super(sources, conversionService, "AnnotationParse");
    }

    static final AnnotationProfilesParseObject getInstance(MutablePropertySources sources, ConfigurableConversionService conversionService) {
        return new AnnotationProfilesParseObject(sources, conversionService);
    }

    public List<Class> parent(Class klass) {
        List<Class> list = new LinkedList();
        while (klass != Object.class) {
            list.add(0, klass = klass.getSuperclass());
        }
        list.remove(klass);
        return list;
    }

    @Override
    public <T> T getGenerate(@NotNull Class<T> toObject) {
        try {
            Constructor<T> defaultConstructor = toObject.getDeclaredConstructor();
            defaultConstructor.setAccessible(true);
            ConfigurationProperties prefix = toObject.getAnnotation(ConfigurationProperties.class);
            ObjectAccess.isEmpty(prefix, "please enter the @Prefix of class");
            T instance = defaultConstructor.newInstance();
            List<Class> classList = parent(toObject);
            classList.add(toObject);
            for (Class klass : classList) {
                for (Field target : klass.getDeclaredFields()) {
                    if (target.getModifiers() != 2) {
                        continue;
                    }
                    try {
                        Method get = toObject.getMethod(StringTools.splicing("get", StringTools.firstHump(target.getName())));
                        if (get.getModifiers() != 1) {
                            continue;
                        }
                    } catch (NoSuchMethodException e) {
                        continue;
                    }
                    Object value = sources.stream().map(propertySource -> propertySource.getProperty(StringTools.splicing(prefix.prefix(), ".", prefix.value(), ".", target.getName()))).filter(ObjectTools::isNonEmpty).findFirst().orElse(null);
                    if (ObjectTools.isNonEmpty(value)) {
                        target.setAccessible(true);
                        if (value.getClass() != target.getType()) {
                            value = conversionService.convert(value, target.getType());
                        }
                        target.set(instance, value);
                    }
                }
            }
            return instance;
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw new ParseException("load property generate fail", e);
        }
    }
}
