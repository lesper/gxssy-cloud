package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import top.latke.conf.DataSourceProxyAutoConfiguration;

/**
 * 订单微服务启动入口
 * 启动依赖组件/中间件： Redis + MySQL + Nacos + Kafka + Zipkin
 * 1.http://localhost:8001/fox-goods-service/swagger-ui.html
 * 2.http://localhost:8001/fox-goods-service/doc.html
 */
@EnableJpaAuditing
@EnableFeignClients
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
@Import(DataSourceProxyAutoConfiguration.class)
public class FoxOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoxOrderApplication.class,args);
    }

}
