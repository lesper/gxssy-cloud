package top.latke.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 授权中心鉴权之后颁发给客户端的 Token
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {

    private String token;

}
