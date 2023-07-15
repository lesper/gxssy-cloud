package top.latke.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.latke.service.NacosClientService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/nacos-client")
public class NacosClientController {

    private final NacosClientService nacosClientService;

    public NacosClientController(NacosClientService nacosClientService) {
        this.nacosClientService = nacosClientService;
    }

    @GetMapping("service-instance")
    public List<ServiceInstance> logNacosClientInfo(@RequestParam(defaultValue = "fox-nacos-client") String serviceId){
        log.info("coming in log nacos client info: [{}]",serviceId);
        return nacosClientService.getNacosClientInfo(serviceId);
    }
}
