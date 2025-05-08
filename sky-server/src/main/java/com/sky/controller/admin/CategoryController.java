package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import com.sky.valid.groups.Add;
import com.sky.valid.groups.Update;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequestMapping("/admin/category")
@Api(tags = "分类管理")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult<Category>> getCategoryList(@Valid CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分类查询，参数为：{}", categoryPageQueryDTO);
        PageResult<Category> categoryPageResult = categoryService.getCategoryList(categoryPageQueryDTO);
        return Result.success(categoryPageResult);
    }

    @PostMapping
    @ApiOperation("新增分类")
    public Result<?> addCategory(@RequestBody @Validated(Add.class) CategoryDTO categoryDTO) {
        log.info("新增分类，参数为：{}", categoryDTO);
        boolean result = categoryService.addCategory(categoryDTO);
        return result ? Result.success() : Result.error("新增失败");
    }

    @PutMapping
    @ApiOperation("修改分类")
    public Result<?> updateCategory(@RequestBody @Validated(Update.class) CategoryDTO categoryDTO) {
        log.info("修改分类，参数为：{}", categoryDTO);
        boolean result = categoryService.updateCategory(categoryDTO);
        return result ? Result.success() : Result.error("更新失败");
    }

    @DeleteMapping
    @ApiOperation("根据ID删除分类")
    public Result<?> delCategory(@RequestParam @ApiParam(value = "分类ID", required = true) Long id) {
        log.info("删除分类，参数为: {}", id);
        boolean result = categoryService.delCategory(id);
        return result ? Result.success() : Result.error("删除失败");
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用、禁用分类")
    public Result<?> changeCategoryStatus(
            @PathVariable
            @ApiParam(value = "分类状态", allowableValues = "0, 1", required = true)
            @Range(max = 1L, message = "status错误")
            Integer status,

            @RequestParam
            @ApiParam(value = "分类ID", required = true)
            Long id
    ) {
        log.info("修改分类状态，状态为：{}，ID为：{}", status, id);
        boolean result = categoryService.changeCategoryStatus(id, status);
        return result ? Result.success() : Result.error("更新失败");
    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> listCategoryByType(
            @RequestParam
            @ApiParam(value = "分类类型", allowableValues = "1, 2", required = true)
            @Range(min = 1L, max = 2L, message = "分类类型错误")
            Integer type
    ) {
        log.info("根据类型查询分类，参数：{}", type);
        List<Category> categories = categoryService.listCategoryByType(type);
        return Result.success(categories);
    }

}
