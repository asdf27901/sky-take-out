package com.sky.controller.user;

import com.sky.entity.Setmeal;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
@Api(tags = "套餐相关接口")
@Slf4j
@Validated
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    /**
     * 条件查询
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    public Result<List<Setmeal>> list(@RequestParam Long categoryId) {
        log.info("根据分类ID查询套餐列表，分类ID：{}", categoryId);
        List<Setmeal> setmealList = setMealService.getSetMealListByCategoryId(categoryId);
        return Result.success(setmealList);
    }

    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable Long id) {
        log.info("根据套餐ID查询包含的菜品列表，套餐ID：{}", id);
        List<DishItemVO> dishItemVOList = setMealService.getDishListBySetMealId(id);
        return Result.success(dishItemVOList);
    }
}
