package top.latke.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.latke.account.BalanceInfo;
import top.latke.dao.EcommerceBalanceDao;
import top.latke.entity.EcommerceBalance;
import top.latke.filter.AccessContext;
import top.latke.service.IBalanceService;
import top.latke.vo.LoginUserInfo;

/**
 * 用户余额相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class BalanceServiceImpl implements IBalanceService {

    private final EcommerceBalanceDao ecommerceBalanceDao;

    public BalanceServiceImpl(EcommerceBalanceDao ecommerceBalanceDao) {
        this.ecommerceBalanceDao = ecommerceBalanceDao;
    }

    @Override
    public BalanceInfo getCurrentUserBalanceInfo() {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        BalanceInfo balanceInfo = new BalanceInfo(loginUserInfo.getId(),0L);

        EcommerceBalance ecommerceBalance = ecommerceBalanceDao.findByUserId(loginUserInfo.getId());

        if (null != ecommerceBalance) {
            balanceInfo.setBalance(ecommerceBalance.getBalance());
        } else {
            EcommerceBalance newEcommerceBalance = new EcommerceBalance();
            newEcommerceBalance.setUserId(loginUserInfo.getId());
            newEcommerceBalance.setBalance(0L);
            log.info("init user balance record: [{}]",ecommerceBalanceDao.save(newEcommerceBalance).getId());
        }

        return balanceInfo;
    }

    @Override
    public BalanceInfo deductBalance(BalanceInfo balanceInfo) {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();
        EcommerceBalance ecommerceBalance = ecommerceBalanceDao.findByUserId(loginUserInfo.getId());

        if (null == ecommerceBalance
                || ecommerceBalance.getBalance() - balanceInfo.getBalance() < 0) {
            throw new RuntimeException("user balance is not enough!");
        }

        Long sourceBalance = ecommerceBalance.getBalance();
        ecommerceBalance.setBalance(ecommerceBalance.getBalance() - balanceInfo.getBalance());
        log.info("deduct balance: [{}],[{}],[{}]",ecommerceBalanceDao.save(ecommerceBalance).getId(),sourceBalance,balanceInfo.getBalance());

        return new BalanceInfo(ecommerceBalance.getUserId(),ecommerceBalance.getBalance());
    }
}
