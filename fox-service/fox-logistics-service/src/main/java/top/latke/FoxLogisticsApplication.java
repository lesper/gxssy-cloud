package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import top.latke.conf.DataSourceProxyAutoConfiguration;

/**
 * 物流微服务启动入口
 * 启动依赖组件/中间件： Redis + MySQL + Nacos + Kafka + Zipkin
 * 1.http://localhost:8001/fox-logistics-service/swagger-ui.html
 * 2.http://localhost:8001/fox-logistics-service/doc.html
 */
@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
@Import(DataSourceProxyAutoConfiguration.class)
public class FoxLogisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoxLogisticsApplication.class,args);
    }

}
