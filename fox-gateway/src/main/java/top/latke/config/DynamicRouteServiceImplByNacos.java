package top.latke.config;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * 通过 Nacos 下发动态路由配置，监听 Nacos 中路由配置变更
 */
@Slf4j
@Component
@DependsOn({ "gatewayConfig" })
public class DynamicRouteServiceImplByNacos {

    /**
     * Nacos 配置服务
     */
    private ConfigService configService;

    private final DynamicRouteServiceImpl dynamicRouteService;

    public DynamicRouteServiceImplByNacos(DynamicRouteServiceImpl dynamicRouteService) {
        this.dynamicRouteService = dynamicRouteService;
    }

    /**
     * 初始化 Nacos Config
     * @return
     */
    private ConfigService initConfigService() {
        try {
            Properties properties = new Properties();
            properties.setProperty("serverAddr",GatewayConfig.NACOS_SERVER_ADDR);
            properties.setProperty("namespace",GatewayConfig.NACOS_NAMESPACE);
            return configService = NacosFactory.createConfigService(properties);
        }catch (Exception ex){
            log.error("init gateway nacos config error: [{}]",ex.getMessage(),ex);
            return null;
        }
    }

    /**
     * 监听 Nacos 下发的动态路由配置
     * @param dataId
     * @param group
     */
    private void dynamicRouteByNacosListener(String dataId, String group) {
        try {

        } catch (Exception ex){

        }
    }


}
