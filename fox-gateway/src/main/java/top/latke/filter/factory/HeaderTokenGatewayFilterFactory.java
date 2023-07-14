package top.latke.filter.factory;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import top.latke.filter.HeaderTokenGatewayFilter;

/**
 * 使 HeaderTokenGatewayFilterFactory 局部过滤器生效,命名必须是 prefix + GatewayFilterFactory，否则会发生找不到的情况
 * 	"filters": [{
 * 			"name": "HeaderToken"
 *                },
 *        {
 * 			"name": "StripPrefix",
 * 			"args": {
 * 				"parts": "1"
 *            }
 *        }
 * 	]
 */
@Component
public class HeaderTokenGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    @Override
    public GatewayFilter apply(Object config) {
        return new HeaderTokenGatewayFilter();
    }
}
