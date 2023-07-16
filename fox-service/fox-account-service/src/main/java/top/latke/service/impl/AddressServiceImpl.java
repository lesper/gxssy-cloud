package top.latke.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.latke.account.AddressInfo;
import top.latke.common.TableId;
import top.latke.dao.EcommerceAddressDao;
import top.latke.entity.EcommerceAddress;
import top.latke.filter.AccessContext;
import top.latke.service.IAddressService;
import top.latke.vo.LoginUserInfo;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户地址相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class AddressServiceImpl implements IAddressService {

    private final EcommerceAddressDao ecommerceAddressDao;

    public AddressServiceImpl(EcommerceAddressDao ecommerceAddressDao) {
        this.ecommerceAddressDao = ecommerceAddressDao;
    }

    /**
     * 存储多个地址信息
     * @param addressInfo
     * @return
     */
    @Override
    public TableId createAddressInfo(AddressInfo addressInfo) {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        //将传递的参数转换为实体对象
        List<EcommerceAddress> ecommerceAddresses = addressInfo.getAddressItems()
                .stream()
                .map(addressItem -> EcommerceAddress.to(loginUserInfo.getId(),addressItem))
                .collect(Collectors.toList());

        //保存数据表
        List<EcommerceAddress> savedRecords = ecommerceAddressDao.saveAll(ecommerceAddresses);
        List<Long> ids = savedRecords.stream()
                .map(EcommerceAddress::getId)
                .collect(Collectors.toList());

        log.info("create address info: [{}],[{}]",loginUserInfo.getId(), JSON.toJSONString(ids));

        return new TableId(ids.stream().map(TableId.Id::new).collect(Collectors.toList()));
    }

    @Override
    public AddressInfo getCurrentAddressInfo() {
        LoginUserInfo loginUserInfo = AccessContext.getLoginUserInfo();

        List<EcommerceAddress> ecommerceAddresses = ecommerceAddressDao.findAllByUserId(loginUserInfo.getId());

        List<AddressInfo.AddressItem> addressItems = ecommerceAddresses.stream()
                .map(EcommerceAddress::toAddressItem)
                .collect(Collectors.toList());

        return new AddressInfo(loginUserInfo.getId(),addressItems);
    }

    @Override
    public AddressInfo getAddressInfoById(Long id) {
        EcommerceAddress ecommerceAddress = ecommerceAddressDao.findById(id).orElse(null);

        if (null == ecommerceAddress) {
            throw new RuntimeException("address is not exist");
        }

        return new AddressInfo(ecommerceAddress.getUserId(), Collections.singletonList(ecommerceAddress.toAddressItem()));
    }

    @Override
    public AddressInfo getAddressInfoByTableId(TableId tableId) {

        List<Long> ids = tableId.getIds().stream()
                .map(TableId.Id::getId)
                .collect(Collectors.toList());

        log.info("get address info by table id: [{}]",JSON.toJSONString(ids));

        List<EcommerceAddress> ecommerceAddresses = ecommerceAddressDao.findAllById(ids);

        if (CollectionUtils.isEmpty(ecommerceAddresses)) {
            return new AddressInfo(-1L, Collections.emptyList());
        }

        List<AddressInfo.AddressItem> addressItems = ecommerceAddresses.stream()
                .map(EcommerceAddress::toAddressItem)
                .collect(Collectors.toList());

        return new AddressInfo(ecommerceAddresses.get(0).getUserId(),addressItems);
    }
}
