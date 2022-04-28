package com.muse.cloud.operate.profile;

import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * description: connect redis info object
 *
 * @Author ZhaoMuse
 * @date 2022/4/4 15:58
 * @Since 1.0
 */
@Slf4j
public class RedisConnectInfo implements Serializable {
    /**
     * redis server port
     */
    private int port = 6379;
    /**
     * redisTemplate Object get in use
     */
    private boolean extendTemplate = false;
    /**
     * service address
     */
    private String ip = "127.0.0.1";
    /**
     * redis authentication userName
     */
    private String user;
    /**
     * redis authentication userPassword
     */
    private String password;

    public int getPort() {
        return port;
    }

    public boolean isExtendTemplate() {
        return extendTemplate;
    }

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
