package top.latke.service;

import cn.hutool.core.codec.Base64;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.latke.util.TokenParseUtil;
import top.latke.vo.LoginUserInfo;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * RSA 非堆成加密算法： 生成公钥和私钥
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class JWTServiceTest {

    @Autowired
    private IJWTService ijwtService;
    @Test
    public void testGenerateAndParseToken() throws Exception{
        String jwtToken = ijwtService.generateToken("admin","e10adc3949ba59abbe56e057f20f883e");
        log.info("jwt token is: [{}]",jwtToken);
        LoginUserInfo loginUserInfo = TokenParseUtil.parseUserInfoFromToken(jwtToken);
        log.info("parse token is: [{}]", JSON.toJSONString(loginUserInfo));
    }

}
