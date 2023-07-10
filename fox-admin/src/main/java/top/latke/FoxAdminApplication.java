package top.latke;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 监控中心服务器启动入口
 */
@EnableAdminServer
@SpringBootApplication
public class FoxAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(FoxAdminApplication.class,args);
    }

}