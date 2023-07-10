package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 授权中心启动入口
 */
@EnableJpaAuditing //允许Jpa自动审计
@EnableDiscoveryClient
@SpringBootApplication
public class FoxAuthorityCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoxAuthorityCenterApplication.class,args);
    }

}