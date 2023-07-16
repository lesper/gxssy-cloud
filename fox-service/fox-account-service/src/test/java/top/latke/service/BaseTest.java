package top.latke.service;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.latke.filter.AccessContext;
import top.latke.vo.LoginUserInfo;

/**
 * 测试用例基类，填充登录用户信息
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public abstract class BaseTest {

    protected final LoginUserInfo loginUserInfo = new LoginUserInfo(10L, "admin");

    @Before
    public void init() {
        AccessContext.setLoginUserInfo(loginUserInfo);
    }

    @After
    public void destroy() {
        AccessContext.clearLoginUserInfo();
    }

}
