package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult<Category> getCategoryList(CategoryPageQueryDTO categoryPageQueryDTO);

    boolean addCategory(CategoryDTO categoryDTO);

    boolean updateCategory(CategoryDTO categoryDTO);

    boolean delCategory(Long id);

    Category getCategoryById(Long id);

    int getCategoryByName(String categoryName);

    boolean changeCategoryStatus(Long id, Integer status);

    List<Category> listCategoryByType(Integer type);

    List<Category> listAllCategory();
}
