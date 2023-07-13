package top.latke.filter.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import top.latke.filter.HeaderTokenGatewayFilter;

/**
 * 使 HeaderTokenGatewayFilter 局部过滤器生效
 */
@Component
public class HeaderTokenGatewayFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new HeaderTokenGatewayFilter();
    }
}
