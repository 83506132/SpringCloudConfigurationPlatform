package org.muse.redis.config.autoconfigure.profile;

import com.muse.cloud.operate.RedisConnectInfo;
import com.muse.tool.util.StringTools;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * description: redis info profile configuration template
 *
 * @Author ZhaoMuse
 * @date 2022/4/3 11:48
 * @Since 1.0
 */
@ToString
@ConditionalOnMissingBean()
@Component
@ConfigurationProperties(prefix = "spring.cloud.config.redis")
@Slf4j
public class RedisConfigureProfile extends RedisConnectInfo implements Serializable {
    /**
     * serialization id
     */
    private static final long serialVersionUID = -2145565534283060484L;
    /**
     * start redis configure service
     */
    private boolean enable = true;
    /**
     * redisTemplate Object get in use
     */
    private boolean extendTemplate = false;
    /**
     * file name suffix
     */
    private String suffix = "yml";
    /**
     * configure file name list
     */
    private List<String> profile = Collections.emptyList();
    /**
     * whole address
     */
    private String host;

    public String host() {
        return host == null ? host = StringTools.splicing(getIp(), ":", getPort()) : host;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    List<String> getProfile() {
        return profile;
    }

    public void setProfile(List<String> profile) {
        this.profile = profile;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setExtendTemplate(boolean extendTemplate) {
        this.extendTemplate = extendTemplate;
    }
}
