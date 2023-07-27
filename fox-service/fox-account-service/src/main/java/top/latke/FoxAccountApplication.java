package top.latke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import top.latke.conf.DataSourceProxyAutoConfiguration;

/**
 * 用户账户微服务启动入口
 * 1.http://localhost:8003/fox-account-service/swagger-ui.html
 * 2.http://localhost:8003/fox-account-service/doc.html
 */
@EnableJpaAuditing
@SpringBootApplication
@EnableDiscoveryClient
@Import(DataSourceProxyAutoConfiguration.class)
public class FoxAccountApplication {

    public static void main(String[] args) {
        SpringApplication.run(FoxAccountApplication.class,args);
    }

}
