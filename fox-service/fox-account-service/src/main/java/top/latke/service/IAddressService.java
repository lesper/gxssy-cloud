package top.latke.service;

import top.latke.account.AddressInfo;
import top.latke.common.TableId;

/**
 * 用户地址相关服务接口定义
 */
public interface IAddressService {

    /**
     * 创建用户地址信息
     * @param addressInfo
     * @return
     */
    TableId createAddressInfo(AddressInfo addressInfo);

    /**
     * 获取当前登录的用户信息
     * @return
     */
    AddressInfo getCurrentAddressInfo();

    /**
     * 获取当前登录的用户信息,根据主键
     * @return
     */
    AddressInfo getAddressInfoById(Long id);

    /**
     * 获取当前登录的用户信息,根据 TableId
     * @return
     */
    AddressInfo getAddressInfoByTableId(TableId tableId);


}
