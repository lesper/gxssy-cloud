package top.latke.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rx.Observable;
import top.latke.service.NacosClientService;
import top.latke.service.hystrix.NacosClientHystrixCommand;
import top.latke.service.hystrix.UseHystrixCommandAndAnnotation;

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

    public HystrixController(UseHystrixCommandAndAnnotation useHystrixCommandAndAnnotation, NacosClientService nacosClientService) {
        this.useHystrixCommandAndAnnotation = useHystrixCommandAndAnnotation;
        this.nacosClientService = nacosClientService;
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
}
