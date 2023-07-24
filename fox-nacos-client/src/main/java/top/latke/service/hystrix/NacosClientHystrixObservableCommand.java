package top.latke.service.hystrix;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import rx.Observable;
import rx.Subscriber;
import top.latke.service.NacosClientService;

import java.util.List;

/**
 * NacosClientService 实现包装
 * Hytrix 舱壁模式，基于信号量实现
 */
@Slf4j
public class NacosClientHystrixObservableCommand extends HystrixObservableCommand<List<ServiceInstance>> {

    /**
     * 要保护的服务
     */
    private final NacosClientService nacosClientService;
    /**
     * 方法传递需要的参数
     */
    private final List<String> serviceIds;

    public NacosClientHystrixObservableCommand(NacosClientService nacosClientService, List<String> serviceIds) {
        super(
                HystrixObservableCommand.Setter.withGroupKey(
                                HystrixCommandGroupKey.Factory.asKey("NacosClientService")
                        ).andCommandKey(HystrixCommandKey.Factory.asKey("NacosClientHystrixObservableCommand"))
                        .andCommandPropertiesDefaults(
                                HystrixCommandProperties.Setter().withFallbackEnabled(true).withCircuitBreakerEnabled(true)
                        )
        );
        this.nacosClientService = nacosClientService;
        this.serviceIds = serviceIds;
    }

    /**
     * 要保护的方法调用
     *
     * @return
     */
    @Override
    protected Observable<List<ServiceInstance>> construct() {
        return Observable.create(new Observable.OnSubscribe<List<ServiceInstance>>() {
            // Observable 有三个关键的事件方法，分别是 onNext，onCompled，onError
            @Override
            public void call(Subscriber<? super List<ServiceInstance>> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {
                        log.info("subscriber command task: [{}], [{}]", JSON.toJSONString(serviceIds), Thread.currentThread().getName());
                        serviceIds.forEach(serviceId -> subscriber.onNext(nacosClientService.getNacosClientInfo(serviceId)));
                        subscriber.onCompleted();
                        log.info("command task completed: [{}], [{}]", JSON.toJSONString(serviceIds), Thread.currentThread().getName());
                    }
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }
        });
    }

    /**
     * 服务降级
     *
     * @return
     */
    @Override
    protected Observable<List<ServiceInstance>> resumeWithFallback() {
        return super.resumeWithFallback();
    }
}
