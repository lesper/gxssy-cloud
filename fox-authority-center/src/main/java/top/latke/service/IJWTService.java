package top.latke.service;

import top.latke.vo.UsernameAndPassword;

/**
 * JWT 相关服务接口定义
 */
public interface IJWTService {

    /**
     * 生成 JWT Token，使用默认的超时时间
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    String generateToken(String username, String password) throws Exception;

    /**
     * 生成 JWT Token，使用指定超时时间
     * @param username
     * @param password
     * @return
     * @throws Exception
     */
    String generateToken(String username, String password, int expire) throws Exception;

    /**
     * 注册用户并生成 Token 返回
     * @param usernameAndPassword
     * @return
     * @throws Exception
     */
    String registerUserAndGenerateToken(UsernameAndPassword usernameAndPassword) throws Exception;

}
