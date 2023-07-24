package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 工程启动入口
 */
@ServletComponentScan
@EnableCircuitBreaker
@EnableFeignClients
//@RefreshScope
@EnableDiscoveryClient
@SpringBootApplication
public class FoxNacosClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoxNacosClientApplication.class,args);
    }

}