package top.latke.service.hystrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import top.latke.service.NacosClientService;

import java.util.Collections;
import java.util.List;

/**
 * 带有缓存功能的 Hystrix
 */
@Slf4j
public class CacheHystrixCommand extends HystrixCommand<List<ServiceInstance>> {

    /**
     * 需要保护的服务
     */
    private final NacosClientService nacosClientService;

    private final String serviceId;

    private static final HystrixCommandKey CACHED_KEY = HystrixCommandKey.Factory.asKey("CacheHystrixCommand");

    public CacheHystrixCommand(NacosClientService nacosClientService,String serviceId) {
        super(
                HystrixCommand.Setter.withGroupKey(
                                HystrixCommandGroupKey.Factory.asKey("CacheHystrixCommandGroup")
                        ).andCommandKey(CACHED_KEY)

        );
        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("CacheHystrixCommand In Hystrix Command to get service instance: [{}],[{}]",this.serviceId,Thread.currentThread().getName());
        return this.nacosClientService.getNacosClientInfo(this.serviceId);
    }

    @Override
    protected List<ServiceInstance> getFallback() {
        return Collections.emptyList();
    }

    @Override
    protected String getCacheKey() {
        return serviceId;
    }

    /**
     * 根据缓存 Key 清理在一次 Hystrix 请求上下文中的缓存
     * @param serviceId
     */
    public static void flushRequestCache(String serviceId) {
        HystrixRequestCache.getInstance(
                CACHED_KEY,
                HystrixConcurrencyStrategyDefault.getInstance()
        ).clear(serviceId);
        log.info("flush request cache in hystrix command: [{}][{}]",serviceId,Thread.currentThread().getName());
    }
}
