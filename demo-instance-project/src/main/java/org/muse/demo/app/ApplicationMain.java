package org.muse.demo.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * description:
 *
 * @Author ZhaoMuse
 * @date 2022/3/29 14:25
 * @Since
 */
@SpringBootApplication
public class ApplicationMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ApplicationMain.class, args);
    }
}
