package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressService;
import com.sky.valid.groups.Add;
import com.sky.valid.groups.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Api(tags = "收货地址相关接口")
@Validated
@RequestMapping("/user/addressBook")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping
    @ApiOperation("新增收货地址")
    public Result<?> saveAddress(@RequestBody @Validated(Add.class) AddressBook addressBook) {
        log.info("新增收货地址：{}", addressBook);
        boolean result = addressService.saveAddress(addressBook);
        return result ? Result.success() : Result.error("添加失败");
    }

    @GetMapping("/list")
    @ApiOperation("获取当前用户所有收货地址")
    public Result<List<AddressBook>> getAllAddress() {
        log.info("查询当前用户所有收货地址，当前用户ID：{}", BaseContext.getCurrentId());
        List<AddressBook> addresses = addressService.getAllAddress();
        return Result.success(addresses);
    }

    @GetMapping("/default")
    @ApiOperation("查询默认地址")
    public Result<AddressBook> getDefaultAddress() {
        log.info("查询当前用户的默认收货地址, 当前用户ID：{}", BaseContext.getCurrentId());
        AddressBook defaultAddress = addressService.getDefaultAddress();
        return Result.success(defaultAddress);
    }

    @PutMapping("/default")
    @ApiOperation("设置为默认地址")
    public Result<?> setDefaultAddress(@RequestBody AddressBook addressBook) {
        log.info("设置默认地址，地址ID：{}", addressBook.getId());
        boolean result = addressService.setDefaultAddress(addressBook.getId());
        return result ? Result.success() : Result.error("设置失败");
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询收货地址")
    public Result<AddressBook> getAddressById(@PathVariable @ApiParam(value = "地址ID", required = true) Long id) {
        log.info("根据ID查询地址，ID为：{}", id);
        AddressBook addressBook = addressService.getAddressById(id);
        return Result.success(addressBook);
    }

    @DeleteMapping
    @ApiOperation("根据ID删除收货地址")
    public Result<?> deleteAddressById(@RequestParam @ApiParam(value = "地址ID", required = true) Long id) {
        log.info("根据ID删除地址，ID为：{}", id);
        boolean result = addressService.deleteAddressById(id);
        return result ? Result.success() : Result.error("删除失败");
    }

    @PutMapping
    @ApiOperation("修改收货地址")
    public Result<?> updateAddress(@RequestBody @Validated(Update.class) AddressBook addressBook) {
        log.info("修改收货地址：{}", addressBook);
        boolean result = addressService.updateAddress(addressBook);
        return result ? Result.success() : Result.error("修改失败");
    }
}
