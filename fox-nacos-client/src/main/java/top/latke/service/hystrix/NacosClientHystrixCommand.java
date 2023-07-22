package top.latke.service.hystrix;

import com.netflix.hystrix.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import top.latke.service.NacosClientService;

import java.util.Collections;
import java.util.List;

/**
 * NacosClientService 实现包装
 * Hytrix 舱壁模式
 * 1.线程池
 * 2.信号量
 */
@Slf4j
public class NacosClientHystrixCommand extends HystrixCommand<List<ServiceInstance>> {

    /**
     * 需要保护的服务
     */
    private final NacosClientService nacosClientService;

    private final String serviceId;

    public NacosClientHystrixCommand(NacosClientService nacosClientService,String serviceId) {
        super(
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
                        .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixCommand"))
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("NacosClientPool"))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter()
                                        .withExecutionIsolationStrategy(
                                                HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                                        .withFallbackEnabled(true)
                                        .withCircuitBreakerEnabled(true)
                        )
        );

        //信号量隔离策略
//        Setter semaphore =
//                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("NacosClientService"))
//                .andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixCommand"))
//                .andCommandPropertiesDefaults(
//                        HystrixCommandProperties.Setter()
//                        .withCircuitBreakerRequestVolumeThreshold(10)
//                        .withCircuitBreakerSleepWindowInMilliseconds(5000)
//                        .withCircuitBreakerErrorThresholdPercentage(50)
//                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)  // 指定使用信号量隔离
//                        //.....
//                );

        this.nacosClientService = nacosClientService;
        this.serviceId = serviceId;
    }

    /**
     * 要保护的方法，调用写在 run 方法中
     * @return
     * @throws Exception
     */
    @Override
    protected List<ServiceInstance> run() throws Exception {
        log.info("NacosClientService In Hystrix Command to Get Service Instance: [{}], [{}]",
                this.serviceId, Thread.currentThread().getName());
        return this.nacosClientService.getNacosClientInfo(this.serviceId);
    }

    /**
     * 降级处理策略
     * @return
     */
    @Override
    protected List<ServiceInstance> getFallback() {
        log.warn("NacosClientService run error: [{}], [{}]",
                this.serviceId, Thread.currentThread().getName());
        return Collections.emptyList();
    }
}
