package top.latke.service;

import top.latke.account.BalanceInfo;

/**
 * 用户余额相关服务接口定义
 */
public interface IBalanceService {

    /**
     * 获取当前用户余额信息
     * @return
     */
    BalanceInfo getCurrentUserBalanceInfo();

    /**
     * 扣减用户余额
     * @param balanceInfo 想要扣减的余额
     * @return
     */
    BalanceInfo deductBalance(BalanceInfo balanceInfo);

}
