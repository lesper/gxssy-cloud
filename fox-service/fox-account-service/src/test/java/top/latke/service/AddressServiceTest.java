package top.latke.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import top.latke.account.AddressInfo;
import top.latke.common.TableId;

import java.util.Collections;

/**
 * 用户地址相关服务功能测试
 */
@Slf4j
public class AddressServiceTest extends BaseTest {

    @Autowired
    private IAddressService addressService;

    @Test
    public void testCreateAddressInfo() {
        AddressInfo.AddressItem addressItem = new AddressInfo.AddressItem();
        addressItem.setUsername("Admin");
        addressItem.setPhone("18888888888");
        addressItem.setProvince("湖北省");
        addressItem.setCity("武汉市");
        addressItem.setAddressDetail("洪山区光谷软件园");

        log.info("test create address info: [{}]", JSON.toJSONString(
                        addressService.createAddressInfo(
                                new AddressInfo(loginUserInfo.getId(), Collections.singletonList(addressItem)))
                )
        );

    }

    @Test
    public void testGetCurrentAddressInfo() {
        log.info("test get current user address info: [{}]", JSON.toJSONString(addressService.getCurrentAddressInfo()));
    }

    @Test
    public void testGetAddressInfoById() {
        log.info("test get byId current user address info: [{}]", JSON.toJSONString(addressService.getAddressInfoById(10L)));
    }

    @Test
    public void testGetAddressInfoByTableId() {
        log.info("test get byTableId current user address info: [{}]", JSON.toJSONString(
                addressService.getAddressInfoByTableId(new TableId(Collections.singletonList(new TableId.Id(10L)))))
        );
    }
}
