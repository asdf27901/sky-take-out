package com.sky.controller.user;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/user/shoppingCart")
@Api(tags = "购物车相关接口")
public class CartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    @ApiOperation("添加购物车")
    public Result<?> add(@RequestBody @Valid ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车，商品信息为：{}", shoppingCartDTO);
        boolean result = shoppingCartService.addShoppingCart(shoppingCartDTO);
        return result ? Result.success() : Result.error("添加失败");
    }

    @GetMapping("/list")
    @ApiOperation("获取购物车列表")
    public Result<List<ShoppingCart>> listShoppingCart() {
        log.info("获取购物车列表, 用户ID：{}", BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartService.listShoppingCart();
        return Result.success(shoppingCartList);
    }

    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result<?> clearShoppingCart() {
        log.info("清空购物车列表，用户ID：{}", BaseContext.getCurrentId());
        boolean result = shoppingCartService.clearShoppingCart();
        return result ? Result.success() : Result.error("清空失败");
    }

    @PostMapping("/sub")
    @ApiOperation("删除购物车")
    public Result<?> sub(@RequestBody @Valid ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车，商品信息为：{}", shoppingCartDTO);
        boolean result = shoppingCartService.subShoppingCart(shoppingCartDTO);
        return result ? Result.success() : Result.error("删除失败");
    }
}
