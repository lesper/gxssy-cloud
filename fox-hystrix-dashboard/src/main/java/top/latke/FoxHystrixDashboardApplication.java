package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * 工程启动入口
 * Hystrix Dashboard: http://localhost:9999/fox-hystrix-dashboard/hystrix
 * nacos client: http://127.0.0.1:8000/fox-nacos-client/actuator/hystrix.stream
 */
@EnableHystrixDashboard
@EnableDiscoveryClient
@SpringBootApplication
public class FoxHystrixDashboardApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoxHystrixDashboardApplication.class,args);
    }

}