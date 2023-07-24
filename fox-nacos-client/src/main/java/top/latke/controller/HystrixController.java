package top.latke.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import rx.Observer;
import top.latke.service.NacosClientService;
import top.latke.service.hystrix.*;
import top.latke.service.hystrix.request_merge.NacosClientCollapseCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Hystrix Controller
 */
@Slf4j
@RestController
@RequestMapping("/hystrix")
public class HystrixController {

    private final UseHystrixCommandAndAnnotation useHystrixCommandAndAnnotation;

    private final NacosClientService nacosClientService;

    private final CacheHystrixCommandAnnotation cacheHystrixCommandAnnotation;

    public HystrixController(UseHystrixCommandAndAnnotation useHystrixCommandAndAnnotation, NacosClientService nacosClientService, CacheHystrixCommandAnnotation cacheHystrixCommandAnnotation) {
        this.useHystrixCommandAndAnnotation = useHystrixCommandAndAnnotation;
        this.nacosClientService = nacosClientService;
        this.cacheHystrixCommandAnnotation = cacheHystrixCommandAnnotation;
    }

    @GetMapping("hystrix-command-annotation")
    public List<ServiceInstance> get(@RequestParam String serviceId) {
        log.info("request nacos client info use annotation:[{}],[{}]",serviceId,Thread.currentThread().getName());
        return useHystrixCommandAndAnnotation.getNacosClientInfoFallback(serviceId);
    }

    @GetMapping("/simple-hystrix-command")
    public List<ServiceInstance> getServiceInstanceByServiceId(
            @RequestParam String serviceId) throws Exception {

        // 第一种方式
        List<ServiceInstance> serviceInstances01 = new NacosClientHystrixCommand(
                nacosClientService, serviceId
        ).execute();    // 同步阻塞
        log.info("use execute to get service instances: [{}], [{}]",
                JSON.toJSONString(serviceInstances01), Thread.currentThread().getName());

        // 第二种方式
        List<ServiceInstance> serviceInstances02;
        Future<List<ServiceInstance>> future = new NacosClientHystrixCommand(
                nacosClientService, serviceId
        ).queue();      // 异步非阻塞
        // 这里可以做一些别的事, 需要的时候再去拿结果
        serviceInstances02 = future.get();
        log.info("use queue to get service instances: [{}], [{}]",
                JSON.toJSONString(serviceInstances02), Thread.currentThread().getName());

        // 第三种方式
        Observable<List<ServiceInstance>> observable = new NacosClientHystrixCommand(
                nacosClientService, serviceId
        ).observe();        // 热响应调用
        List<ServiceInstance> serviceInstances03 = observable.toBlocking().single();
        log.info("use observe to get service instances: [{}], [{}]",
                JSON.toJSONString(serviceInstances03), Thread.currentThread().getName());

        // 第四种方式
        Observable<List<ServiceInstance>> toObservable = new NacosClientHystrixCommand(
                nacosClientService, serviceId
        ).toObservable();        // 异步冷响应调用
        List<ServiceInstance> serviceInstances04 = toObservable.toBlocking().single();
        log.info("use toObservable to get service instances: [{}], [{}]",
                JSON.toJSONString(serviceInstances04), Thread.currentThread().getName());

        // execute = queue + get
        return serviceInstances01;
    }

    @GetMapping("/hystrix-observable-command")
    public List<ServiceInstance> getServiceInstancesByServiceIdObservable(@RequestParam String serviceId) {
        List<String> serviceIds = Arrays.asList(serviceId,serviceId,serviceId);
        List<List<ServiceInstance>> result = new ArrayList<>();
        NacosClientHystrixObservableCommand observableCommand = new NacosClientHystrixObservableCommand(nacosClientService,serviceIds);
        //异步执行命令
        Observable<List<ServiceInstance>> observable = observableCommand.observe();

        //注册获取结果
        observable.subscribe(new Observer<List<ServiceInstance>>() {
            @Override
            public void onCompleted() {
                log.info("all tasks is complete:[{}],[{}]",serviceId,Thread.currentThread().getName());
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onNext(List<ServiceInstance> serviceInstances) {
                result.add(serviceInstances);
            }
        });

        log.info("observable command result is: [{}],[{}]",JSON.toJSONString(result),Thread.currentThread().getName());
        return result.get(0);
    }

    @GetMapping("/cache-hystrix-command")
    public void cacheHystrixCommand(@RequestParam String serviceId) {
        //使用缓存 Command 发起两次请求
        CacheHystrixCommand cacheHystrixCommand1 = new CacheHystrixCommand(nacosClientService,serviceId);
        CacheHystrixCommand cacheHystrixCommand2 = new CacheHystrixCommand(nacosClientService,serviceId);
        List<ServiceInstance> execute1 = cacheHystrixCommand1.execute();
        List<ServiceInstance> execute2 = cacheHystrixCommand2.execute();
        log.info("result1,result2:[{}],[{}]",JSON.toJSONString(execute1),JSON.toJSONString(execute2));

        CacheHystrixCommand.flushRequestCache(serviceId);

        CacheHystrixCommand cacheHystrixCommand3 = new CacheHystrixCommand(nacosClientService,serviceId);
        CacheHystrixCommand cacheHystrixCommand4 = new CacheHystrixCommand(nacosClientService,serviceId);

        List<ServiceInstance> execute3 = cacheHystrixCommand3.execute();
        List<ServiceInstance> execute4 = cacheHystrixCommand4.execute();
        log.info("result3,result4:[{}],[{}]",JSON.toJSONString(execute3),JSON.toJSONString(execute4));
    }

    @GetMapping("/cache-annotation-01")
    public List<ServiceInstance> useCacheByAnnotation01(@RequestParam String serviceId) {
        log.info("use cache01 by useCacheByAnnotation01(Controller): [{}]",serviceId);
        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        List<ServiceInstance> result02 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);

        cacheHystrixCommandAnnotation.flushCacheByAnnotation01(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        List<ServiceInstance> result04 = cacheHystrixCommandAnnotation.useCacheByAnnotation01(serviceId);
        return result04;
    }

    @GetMapping("/cache-annotation-02")
    public List<ServiceInstance> useCacheByAnnotation02(@RequestParam String serviceId) {
        log.info("use cache02 by useCacheByAnnotation02(Controller): [{}]",serviceId);
        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        List<ServiceInstance> result02 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);

        cacheHystrixCommandAnnotation.flushCacheByAnnotation02(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        List<ServiceInstance> result04 = cacheHystrixCommandAnnotation.useCacheByAnnotation02(serviceId);
        return result04;
    }

    @GetMapping("/cache-annotation-03")
    public List<ServiceInstance> useCacheByAnnotation03(@RequestParam String serviceId) {
        log.info("use cache03 by useCacheByAnnotation03(Controller): [{}]",serviceId);
        List<ServiceInstance> result01 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        List<ServiceInstance> result02 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);

        cacheHystrixCommandAnnotation.flushCacheByAnnotation03(serviceId);

        List<ServiceInstance> result03 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        List<ServiceInstance> result04 = cacheHystrixCommandAnnotation.useCacheByAnnotation03(serviceId);
        return result04;
    }

    /**
     * 编程方式实现请求合并
     */
    @GetMapping("/request-merge")
    public void requestMerge() throws Exception{
        //前三个请求会被合并
        NacosClientCollapseCommand clientCollapseCommand01 = new NacosClientCollapseCommand(nacosClientService,"fox-nacos-client1");
        NacosClientCollapseCommand clientCollapseCommand02 = new NacosClientCollapseCommand(nacosClientService,"fox-nacos-client2");
        NacosClientCollapseCommand clientCollapseCommand03 = new NacosClientCollapseCommand(nacosClientService,"fox-nacos-client3");

        Future<List<ServiceInstance>> future01 = clientCollapseCommand01.queue();
        Future<List<ServiceInstance>> future02 = clientCollapseCommand02.queue();
        Future<List<ServiceInstance>> future03 = clientCollapseCommand03.queue();

        future01.get();
        future02.get();
        future03.get();

        Thread.sleep(2000);

        //过了合并的时间窗口，第四个请求单独发起
        NacosClientCollapseCommand clientCollapseCommand04 = new NacosClientCollapseCommand(nacosClientService,"fox-nacos-client4");
        Future<List<ServiceInstance>> future04 = clientCollapseCommand04.queue();
        future04.get();
    }
}
