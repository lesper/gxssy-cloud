package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 工程启动入口
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class FoxSentinelClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoxSentinelClientApplication.class,args);
    }

}