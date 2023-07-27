package top.latke.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.latke.account.BalanceInfo;
import top.latke.vo.CommonResponse;

/**
 * 用户账户服务 Feign 接口
 */
@FeignClient(contextId = "NotSecuredBalanceClient",value = "fox-order-service")
public interface NotSecuredBalanceClient {


    @RequestMapping(value = "/fox-account-service/balance/deduct-balance",method = RequestMethod.PUT)
    CommonResponse<BalanceInfo> deductBalance(@RequestBody BalanceInfo balanceInfo);
}
