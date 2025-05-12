package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.valid.groups.Add;
import com.sky.valid.groups.Update;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
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

    @ApiOperation("根据ID查询套餐")
    @GetMapping("{id}")
    public Result<SetmealVO> getSetMealById(@PathVariable Long id) {
        log.info("根据ID查询套餐：{}", id);
        SetmealVO setmealVO = setMealService.getSetMealById(id);
        return Result.success(setmealVO);
    }

    @ApiOperation("修改套餐")
    @PutMapping
    public Result<?> updateSetMeal(@RequestBody @Validated(Update.class) SetmealDTO setmealDTO) {
        log.info("更新套餐: {}", setmealDTO);
        boolean result = setMealService.updateSetMeal(setmealDTO);
        return result ? Result.success() : Result.error("更新失败");
    }

    @ApiOperation("修改套餐状态")
    @PostMapping("/status/{status}")
    public Result<?> updateSetMealStatus(
            @PathVariable
            @ApiParam(value = "套餐状态", allowableValues = "0, 1", required = true)
            @Range(max = 1L, message = "状态错误")
            Integer status,

            @ApiParam(value = "套餐ID", required = true)
            @RequestParam
            Long id
    ) {
        log.info("修改套餐状态，套餐ID：{}，状态：{}", id, status);
        boolean result = setMealService.updateSetMealStatus(id, status);
        return result ? Result.success() : Result.error("更新失败");
    }
}
