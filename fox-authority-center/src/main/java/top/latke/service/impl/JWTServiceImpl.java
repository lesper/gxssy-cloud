package top.latke.service.impl;

import cn.hutool.core.codec.Base64Decoder;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import top.latke.constant.AuthorityConstant;
import top.latke.constant.CommonConstant;
import top.latke.dao.EcommerceUserDao;
import top.latke.entity.EcommerceUser;
import top.latke.service.IJWTService;
import top.latke.vo.LoginUserInfo;
import top.latke.vo.UsernameAndPassword;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

/**
 * JWT 相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class JWTServiceImpl implements IJWTService {

    private final EcommerceUserDao ecommerceUserDao;

    public JWTServiceImpl(EcommerceUserDao ecommerceUserDao) {
        this.ecommerceUserDao = ecommerceUserDao;
    }

    @Override
    public String generateToken(String username, String password) throws Exception {
        return null;
    }

    @Override
    public String generateToken(String username, String password, int expire) throws Exception {

        EcommerceUser ecommerceUser = ecommerceUserDao.findByUsernameAndPassword(username,password);

        if (null == ecommerceUser) {
            log.error("can not find user: [{}],[{}]",username,password);
            return null;
        }

        LoginUserInfo loginUserInfo = new LoginUserInfo(ecommerceUser.getId(),ecommerceUser.getUsername());
        if (expire <= 0) {
            expire = AuthorityConstant.DEFAULT_EXPIRE_DAY;
        }

        ZonedDateTime zonedDateTime = LocalDate.now().plus(expire, ChronoUnit.DAYS).atStartOfDay(ZoneId.systemDefault());
        Date expireDate = Date.from(zonedDateTime.toInstant());

        return Jwts.builder().claim(CommonConstant.JWT_USER_INFO_KEY, JSON.toJSONString(loginUserInfo)) //jwt payload --> KV
                .setId(UUID.randomUUID().toString()) //jwt id
                .setExpiration(expireDate) //过期时间
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256) //签名 --> 加密
                .compact();
    }

    @Override
    public String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception {
        return null;
    }

    /**
     * 根据本地 PrivateKey 生成 对象
     * @return
     * @throws Exception
     */
    private PrivateKey getPrivateKey() throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(new BASE64Decoder().decodeBuffer(AuthorityConstant.PRIVATE_KEY));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }
}
