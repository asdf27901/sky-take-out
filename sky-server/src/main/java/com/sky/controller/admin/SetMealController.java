package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.valid.groups.Add;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin/setmeal")
@Api(tags = "套餐相关接口")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @ApiOperation("新增套餐")
    @PostMapping
    public Result<?> saveSetMeal(@RequestBody @Validated(Add.class) SetmealDTO setmealDTO) {
        log.info("新增套餐：{}", setmealDTO);
        boolean result = setMealService.saveSetMeal(setmealDTO);
        return result ? Result.success() : Result.error("添加失败");
    }

    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult<SetmealVO>> listAllSetMeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐分页查询: {}", setmealPageQueryDTO);
        PageResult<SetmealVO> setmealVOPageResult = setMealService.listAllSetMeal(setmealPageQueryDTO);
        return Result.success(setmealVOPageResult);
    }
}
