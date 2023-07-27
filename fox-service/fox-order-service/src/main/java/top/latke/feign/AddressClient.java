package top.latke.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import top.latke.account.AddressInfo;
import top.latke.common.TableId;
import top.latke.feign.hystrix.AddressClientHystrix;
import top.latke.vo.CommonResponse;

/**
 * 用户账户服务 Feign 接口定义（啊暖的）
 */
@FeignClient(contextId = "AddressClient",value = "fox-account-service",fallbackFactory = AddressClientHystrix.class)
public interface AddressClient {

    /**
     * 根据 id 查询地址信息
     * @param tableId
     * @return
     */
    @RequestMapping(value = "/fox-account-service/address/address-info-by-table-id",method = RequestMethod.POST)
    CommonResponse<AddressInfo> getAddressInfoByTableId(@RequestBody TableId tableId);

}
