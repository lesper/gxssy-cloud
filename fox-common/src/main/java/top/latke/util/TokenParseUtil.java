package top.latke.util;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import sun.misc.BASE64Decoder;
import top.latke.constant.CommonConstant;
import top.latke.vo.LoginUserInfo;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.security.spec.X509EncodedKeySpec;
import java.util.Calendar;

/**
 * JWT Token 解析工具类
 */
public class TokenParseUtil {

    /**
     * 从 JWT Token 中解析 LoginUserInfo 对象
     * @param token
     * @return
     * @throws Exception
     */
    public static LoginUserInfo parseUserInfoFromToken(String token) throws Exception {
        if (null == token) {
            return null;
        }

        Jws<Claims> claimsJws = parseToken(token,getPublicKey());
        Claims body = claimsJws.getBody();
        //如果 Token 已经过期，返回 null
        if (body.getExpiration().before(Calendar.getInstance().getTime())) {
            return null;
        }

        return JSON.parseObject(body.get(CommonConstant.JWT_USER_INFO_KEY).toString(),LoginUserInfo.class);
    }

    /**
     * 通过公钥去解析 JWT Token
     * @param token
     * @param publicKey
     * @return
     */
    private static Jws<Claims> parseToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 根据本地 PublicKey 生成 对象
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicKey() throws Exception {
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(new BASE64Decoder().decodeBuffer(CommonConstant.PUBLIC_KEY));
        return KeyFactory.getInstance("RSA").generatePublic(x509EncodedKeySpec);
    }
}
