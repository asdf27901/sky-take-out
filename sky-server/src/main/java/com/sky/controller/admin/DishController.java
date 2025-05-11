package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.valid.groups.Add;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

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

    @ApiOperation("删除菜品")
    @DeleteMapping
    public Result<?> deleteDish(
            @RequestParam
            @ApiParam("需要删除的菜品ID")
            @NotEmpty(message = "菜品ID不能为空")
            Long[] ids
    ) {
        log.info("删除菜品：{}", Arrays.toString(ids));
        boolean result = dishService.deleteDishByIds(ids);
        return result ? Result.success() : Result.error("删除失败");
    }
}
