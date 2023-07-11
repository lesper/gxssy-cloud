package top.latke.service;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.latke.dao.EcommerceUserDao;
import top.latke.entity.EcommerceUser;

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
public class RSATest {

    @Test
    public void generateKeyBytes() throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        log.info("publicKey: [{}]", Base64.encode(publicKey.getEncoded()));
        log.info("privateKey: [{}]", Base64.encode(privateKey.getEncoded()));
    }

}
