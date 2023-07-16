package top.latke.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.latke.account.BalanceInfo;

/**
 * 用户余额相关服务功能测试
 */
@Slf4j
public class BalanceServiceTest extends BaseTest {

    @Autowired
    private IBalanceService balanceService;

    @Test
    public void testGetCurrentBalanceInfo() {
        log.info("test current user balance info: [{]]", JSON.toJSONString(balanceService.getCurrentUserBalanceInfo()));
    }

    @Test
    public void testdeductBalance() {
        BalanceInfo balanceInfo = new BalanceInfo();
        balanceInfo.setUserId(10L);
        balanceInfo.setBalance(1000L);
        log.info("test deduct user balance info: [{}]", JSON.toJSONString(balanceService.deductBalance(balanceInfo)));
    }

}
