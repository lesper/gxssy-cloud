package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 授权中心启动入口
 */
@SpringBootApplication
public class FoxAuthorityCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoxAuthorityCenterApplication.class,args);
    }

}