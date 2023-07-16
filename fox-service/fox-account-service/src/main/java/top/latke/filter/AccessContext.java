package top.latke.filter;

import top.latke.vo.LoginUserInfo;

/**
 * 使用 ThreadLocal 去单独存储每一个线程携带的 LoginUserInfo 信息
 * 要及时的清理保存到 ThreadLocal 中的用户信息
 * 1.保证没有资源泄露
 * 2.保证线程在重用时，不会出现数据混乱
 */
public class AccessContext {

    private static final ThreadLocal<LoginUserInfo> loginUserInfo = new ThreadLocal<>();

    public static LoginUserInfo getLoginUserInfo(){
        return loginUserInfo.get();
    }

    public static void setLoginUserInfo(LoginUserInfo _loginUserInfo) {
        loginUserInfo.set(_loginUserInfo);
    }

    public static void clearLoginUserInfo() {
        loginUserInfo.remove();
    }

}
