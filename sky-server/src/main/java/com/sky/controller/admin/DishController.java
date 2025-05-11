package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.valid.groups.Add;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("/admin/dish")
@Validated
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @ApiOperation("新增菜品")
    @PostMapping
    public Result<?> saveDish(@RequestBody @Validated(Add.class) DishDTO dishDTO) {
        boolean result = dishService.saveDish(dishDTO);
        return result ? Result.success() : Result.error("添加菜品失败");
    }

    @ApiOperation("分页查询菜品列表")
    @GetMapping("/page")
    public Result<PageResult<DishVO>> getDishList(@Valid DishPageQueryDTO dishPageQueryDTO) {
        PageResult<DishVO> dishVOPageResult = dishService.getDishList(dishPageQueryDTO);
        return Result.success(dishVOPageResult);
    }
}
