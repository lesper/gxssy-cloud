package top.latke.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.latke.annotation.IgnoreResponseAdvice;
import top.latke.service.IJWTService;
import top.latke.vo.JwtToken;
import top.latke.vo.UsernameAndPassword;

/**
 * 对外暴露的授权服务接口
 */
@Slf4j
@RestController
@RequestMapping("authority")
public class AuthorityController {

    private final IJWTService ijwtService;

    public AuthorityController(IJWTService ijwtService) {
        this.ijwtService = ijwtService;
    }

    /**
     * 从授权中心获取 Token，且返回信息中没有统一响应的包装
     * @param usernameAndPassword
     * @return
     * @throws Exception
     */
    @IgnoreResponseAdvice
    @PostMapping("token")
    public JwtToken token(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception{
        log.info("method-token request to get token with param: [{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken(ijwtService.generateToken(usernameAndPassword.getUsername(),usernameAndPassword.getPassword()));
    }

    /**
     * 注册用户并从授权中心获取 Token，且返回信息中没有统一响应的包装
     * @param usernameAndPassword
     * @return
     * @throws Exception
     */
    @IgnoreResponseAdvice
    @PostMapping("register")
    public JwtToken register(@RequestBody UsernameAndPassword usernameAndPassword) throws Exception{
        log.info("method-register request to get token with param: [{}]", JSON.toJSONString(usernameAndPassword));
        return new JwtToken(ijwtService.registerUserAndGenerateToken(usernameAndPassword));
    }

}
