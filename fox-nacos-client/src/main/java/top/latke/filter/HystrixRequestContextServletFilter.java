package top.latke.filter;

import com.netflix.hystrix.strategy.HystrixPlugins;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategy;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifier;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import com.netflix.hystrix.strategy.metrics.HystrixMetricsPublisher;
import com.netflix.hystrix.strategy.properties.HystrixPropertiesStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * 初始化 Hystrix 请求上下文环境
 */
@Slf4j
@Component
@WebFilter(
        filterName = "HystrixRequestContextServletFilter",
        urlPatterns = "/",
        asyncSupported = true
)
public class HystrixRequestContextServletFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //初始化 Hystrix 请求上下文
        //在不同的 context 中缓存是不共享的
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            hystrixConcurrencyStrategyConfig();
            filterChain.doFilter(servletRequest,servletResponse);
        }catch (Exception ex){
            context.shutdown();
        }
    }

    /**
     * 配置 Hystrix 的并发策略
     */
    public void hystrixConcurrencyStrategyConfig() {
        try {
            HystrixConcurrencyStrategy target = HystrixConcurrencyStrategyDefault.getInstance();
            HystrixConcurrencyStrategy strategy = HystrixPlugins.getInstance().getConcurrencyStrategy();
            if (target instanceof HystrixConcurrencyStrategy) {
                return;
            }

            //将原来其他的配置保存下来
            HystrixCommandExecutionHook commandExecutionHook = HystrixPlugins.getInstance().getCommandExecutionHook();
            HystrixEventNotifier eventNotifier = HystrixPlugins.getInstance().getEventNotifier();
            HystrixMetricsPublisher metricsPublisher = HystrixPlugins.getInstance().getMetricsPublisher();
            HystrixPropertiesStrategy propertiesStrategy = HystrixPlugins.getInstance().getPropertiesStrategy();

            //先重置
            HystrixPlugins.reset();
            //填充
            HystrixPlugins.getInstance().registerConcurrencyStrategy(target);
            HystrixPlugins.getInstance().registerCommandExecutionHook(commandExecutionHook);
            HystrixPlugins.getInstance().registerEventNotifier(eventNotifier);
            HystrixPlugins.getInstance().registerMetricsPublisher(metricsPublisher);
            HystrixPlugins.getInstance().registerPropertiesStrategy(propertiesStrategy);
            log.info("config hystrix concurrency strategy succes");

        }catch (Exception ex) {
            log.error("Failed to register Hystrix Concurrency Strategy: [{}]",ex.getMessage(),ex);
        }
    }
}
