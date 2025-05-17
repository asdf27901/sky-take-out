package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BusinessException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public Category getCategoryById(Long id) {
        return categoryMapper.getCategoryById(id);
    }

    @Override
    public int getCategoryByName(String categoryName) {
        return categoryMapper.getCategoryByName(categoryName);
    }

    @Override
    public boolean changeCategoryStatus(Long id, Integer status) {
        //查找id是否存在
        Category category = getCategoryById(id);
        if (category == null) throw new BusinessException("分类ID不存在");

        category.setStatus(status);
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        int affectRow = categoryMapper.updateCategory(category);

        return affectRow > 0;
    }

    @Override
    public List<Category> listCategoryByType(Integer type) {
        return categoryMapper.listCategoryByType(type);
    }

    @Override
    public List<Category> listAllCategory() {
        return categoryMapper.listAllCategory();
    }

    @Override
    public PageResult<Category> getCategoryList(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Category> categoryPage = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize())
                .doSelectPage(() -> categoryMapper.getCategoryList(categoryPageQueryDTO));
        return new PageResult<>(categoryPage.getTotal(), categoryPage.getResult(), categoryPage.getPageSize(), categoryPage.getPageNum());
    }

    @Override
    public boolean addCategory(CategoryDTO categoryDTO) {
        // 分类名称要唯一
        // 根据分类名称查找分类
        int count = getCategoryByName(categoryDTO.getName());
        if (count > 0) throw new BusinessException("分类名称已存在，请重新输入");

        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE);
//        category.setCreateTime(LocalDateTime.now());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateUser(BaseContext.getCurrentId());

        int affectRow = categoryMapper.addCategory(category);
        return affectRow > 0;
    }

    @Override
    public boolean updateCategory(CategoryDTO categoryDTO) {
        //查找id是否存在
        Category category = getCategoryById(categoryDTO.getId());
        if (category == null) throw new BusinessException("分类ID不存在");

        // 检查dto中的分类名称和数据库中查出来的分类名称是否一致
        // 如果一致就不需要处理，不一致则需要查询是否已经使用过
        if (!categoryDTO.getName().equals(category.getName())) {
            int count = getCategoryByName(categoryDTO.getName());
            if (count > 0) throw new BusinessException("分类名称已存在，请重新输入");
        }

        BeanUtils.copyProperties(categoryDTO, category);
//        category.setUpdateUser(BaseContext.getCurrentId());
//        category.setUpdateTime(LocalDateTime.now());

        int affectRow = categoryMapper.updateCategory(category);
        return affectRow > 0;
    }

    @Override
    public boolean delCategory(Long id) {
        //查找id是否存在
        Category category = getCategoryById(id);
        if (category == null) throw new BusinessException("分类ID不存在");

        //查询分类下是否存在菜品
        int dishCount = dishMapper.getCountByCategoryId(id);
        int setMealCount = setMealMapper.getCountByCategoryId(id);
        if (dishCount > 0 || setMealCount > 0) {
            throw new DeletionNotAllowedException("删除失败，当前分类下存在套餐或菜品，请检查");
        }

        int affectRow = categoryMapper.delCategoryById(id);
        return affectRow > 0;
    }
}
