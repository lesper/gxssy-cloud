package top.latke.service;

import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Slf4j
@Service
public class NacosClientService {

    private final DiscoveryClient discoveryClient;

    public NacosClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public List<ServiceInstance> getNacosClientInfo(String serviceId) {
        log.info("request nacos client info: [{}]",serviceId);
        return discoveryClient.getInstances(serviceId);
    }

    /**
     * 提供给编程方式的 Hystrix 请求合并
     * @param serviceIds
     * @return
     */
    public List<List<ServiceInstance>> getNacosClientInfos(List<String> serviceIds) {
        log.info("request nacos client to get service instance infos: [{}]", JSON.toJSONString(serviceIds));
        List<List<ServiceInstance>> result = new ArrayList<>();
        serviceIds.forEach(serviceId -> result.add(discoveryClient.getInstances(serviceId)));
        return result;
    }

    //使用注解实现 Hystrix 请求合并
    @HystrixCollapser(batchMethod = "findNacosClientInfos",
            scope = com.netflix.hystrix.HystrixCollapser.Scope.GLOBAL,
            collapserProperties = {
                @HystrixProperty(name = "timerDelayInMilliseconds",value = "300")
            }
    )
    public Future<List<ServiceInstance>> findNacosClientInfo(String serviceId) {
        //系统运行正常，不会走这个方法
        throw new RuntimeException("This method body should not be executed!");
    }

    @HystrixCommand
    public List<List<ServiceInstance>> findNacosClientInfos(List<String> serviceIds) {
        log.info("(findNacosClientInfos)request nacos client to get service instance infos: [{}]", JSON.toJSONString(serviceIds));
        return getNacosClientInfos(serviceIds);
    }
}
