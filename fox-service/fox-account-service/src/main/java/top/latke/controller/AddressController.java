package top.latke.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.latke.account.AddressInfo;
import top.latke.common.TableId;
import top.latke.service.IAddressService;

/**
 * 用户地址服务 Controller
 */
@Api(tags = "用户地址服务")
@Slf4j
@RestController
@RequestMapping("/address")
public class AddressController {

    private final IAddressService addressService;

    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }

    @ApiOperation(value = "创建",notes = "创建用户地址信息",httpMethod = "POST")
    @PostMapping("/create-address")
    public TableId createAddressInfo(@RequestBody AddressInfo addressInfo) {
        return addressService.createAddressInfo(addressInfo);
    }

    @ApiOperation(value = "当前用户",notes = "获取当前登录用户地址信息",httpMethod = "GET")
    @GetMapping("/current-address")
    public AddressInfo getCurrentAddressInfo() {
        return addressService.getCurrentAddressInfo();
    }

    @ApiOperation(value = "获取用户地址信息",notes = "通过 id 获取用户地址信息",httpMethod = "GET")
    @GetMapping ("/address-info")
    public AddressInfo getAddressInfoById(@RequestParam Long id) {
        return addressService.getAddressInfoById(id);
    }

    @ApiOperation(value = "获取用户地址信息",notes = "通过 TableId 获取用户地址信息",httpMethod = "POST")
    @PostMapping("/address-info-by-table-id")
    public AddressInfo getAddressInfoByTableId(@RequestBody TableId tableId) {
        return addressService.getAddressInfoByTableId(tableId);
    }

}
