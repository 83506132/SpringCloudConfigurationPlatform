package org.muse.redis.config.autoconfigure.profile;

import com.muse.tool.util.StringTools;
import lombok.extern.slf4j.Slf4j;
import org.muse.redis.config.autoconfigure.ProfileInfoFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/4/3 23:55
 * @Since
 */
@Slf4j
class RedisConfigureProfileFactory extends ProfileInfoFactory<RedisConfigureProfile> {
    public RedisConfigureProfileFactory(ConfigurableEnvironment environment) {
        super(environment);
    }

    @Override
    public RedisConfigureProfile prepareCreate() {
        RedisConfigureProfile instance = AnnotationProfilesParseObject.getInstance(propertySources, conversionService).getGenerate(RedisConfigureProfile.class);
        List<String> names = Arrays.stream(activeProfiles).map(profile -> StringTools.splicing(prefix, "-", profile, ".", instance.getSuffix())).distinct().collect(Collectors.toList());
        names.add(0, StringTools.splicing(prefix, ".", instance.getSuffix()));
        instance.setProfile(names);
        return instance;
    }
}
