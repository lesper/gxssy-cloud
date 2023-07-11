package top.latke.service;

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

/**
 * EcommerceUser 相关测试
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class EcommerceUserTest {

    @Autowired
    private EcommerceUserDao ecommerceUserDao;

    @Test
    public void createRecord(){

        EcommerceUser ecommerceUser = new EcommerceUser();
        ecommerceUser.setUsername("fox-admin");
        ecommerceUser.setPassword(MD5.create().digestHex("123456"));
        ecommerceUser.setExtraInfo("{}");
        log.info("save user: [{}]", JSON.toJSON(ecommerceUserDao.save(ecommerceUser)));

    }

    @Test
    public void findRecord(){

        log.info("find user fox-admin: [{}]", JSON.toJSON(ecommerceUserDao.findByUsername("fox-admin")));
        log.info("find user: [{}]", JSON.toJSON(ecommerceUserDao.findAll()));

    }
}
