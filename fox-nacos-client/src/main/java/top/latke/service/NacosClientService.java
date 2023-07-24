package top.latke.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

}
