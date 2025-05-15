package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@Slf4j
@Api(tags = "店铺操作接口")
@RequestMapping("/admin/shop")
@Validated
public class ShopController {

    @Autowired
    private ShopService shopService;

    @GetMapping("/status")
    @ApiOperation("后台获取营业状态")
    public Result<Integer> getShopStatus() {
        Integer shopStatus = shopService.getShopStatus();
        log.info("后台获取店铺营业状态: {}", shopStatus == 1 ? "营业中" : "打烊中");
        return Result.success(shopStatus);
    }

    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result<?> setShopStatus(@PathVariable @Range(max = 1L, message = "status不合法") Integer status) {
        log.info("后台设置店铺营业状态：{}", status == 1 ? "营业中" : "打烊中");
        shopService.setShopStatus(status);
        return Result.success();
    }
}
