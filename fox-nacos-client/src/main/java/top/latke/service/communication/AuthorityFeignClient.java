package top.latke.service.communication;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.latke.service.communication.hystrix.AuthorityFeignClientFallbackFactory;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 * 与 Authority 通信的 Feign Client 接口定义
 */
//@FeignClient(contextId = "AuthorityFeignClient",value = "fox-authority-center",fallback = AuthorityFeignClientFallback.class)
@FeignClient(contextId = "AuthorityFeignClient",value = "fox-authority-center",fallbackFactory = AuthorityFeignClientFallbackFactory.class)
public interface AuthorityFeignClient {

    /**
     * 通过 OpenFeign 访问 Authority 获取 Token
     * @param usernameAndPassword
     * @return
     */
    @RequestMapping(value = "/fox-authority-center/authority/token",method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    JwtToken getTokenByFeign(@RequestBody UsernameAndPassword usernameAndPassword);

}
