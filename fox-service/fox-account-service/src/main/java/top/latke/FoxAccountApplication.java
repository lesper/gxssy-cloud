package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 用户账户微服务启动入口
 */
@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
public class FoxAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoxAccountApplication.class,args);
    }

}
