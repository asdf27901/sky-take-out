package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.BusinessException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    public PageResult<DishVO> getDishList(DishPageQueryDTO dishPageQueryDTO) {
        Page<DishVO> dishVoPage = PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize())
                .doSelectPage(() -> dishMapper.getDishVoList(dishPageQueryDTO));
        return new PageResult<>(dishVoPage.getTotal(), dishVoPage.getResult(), dishVoPage.getPageSize(), dishVoPage.getPageNum());
    }

    @Override
    @Transactional // 涉及多表操作，需要开启事务，需要注意避免出现长事务
    public boolean saveDish(DishDTO dishDTO) {
        // 查找菜品名称是否重复
        Dish dish = dishMapper.getDishByDishName(dishDTO.getName());
        if (dish != null) {
            throw new BusinessException("菜品名称重复");
        }

        // 查找分类ID是否存在
        Category category = categoryMapper.getCategoryById(dishDTO.getCategoryId());
        if (category == null) {
            throw new BusinessException("分类ID不存在");
        }

        dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        int affectRow = dishMapper.saveDish(dish);

        // 插入口味表
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        int affectRows = 0;
        if (!CollectionUtils.isEmpty(dishFlavors)) {
            Long dishID = dish.getId();
            dishFlavors.forEach(dishFlavor -> dishFlavor.setDishId(dishID));
            // 口味集合不为空时才进行插入
            affectRows = dishFlavorMapper.saveBatch(dishFlavors);
        }

        return affectRow > 0 && affectRows >= 0;
    }

    @Override
    @Transactional
    public boolean deleteDishByIds(List<Long> ids) {
        // 查找需要被删除的菜品ID是否是起售中
        List<Long> sellingDishIds = dishMapper.getSellingDishListByIds(ids);
        if (sellingDishIds.size() != 0) {
            throw new DeletionNotAllowedException("删除失败，菜品ID为：" + sellingDishIds + " 状态为起售中");
        }
        // 查找菜品ID是否存在关联的套餐
        List<Long> setMealWithDish = setMealDishMapper.getCountByDishIds(ids);
        if (setMealWithDish.size() != 0) {
            throw new DeletionNotAllowedException("删除失败，菜品ID为：" + setMealWithDish + " 存在关联套餐");
        }
        // 删除菜品
        int affectRows = dishMapper.deleteByIds(ids);

        // 删除菜品对应的口味数据
        affectRows *= dishFlavorMapper.deleteByDishIds(ids);

        // TODO：删除阿里云oss对应文件

        return affectRows >= 0;
    }

    @Override
    public DishVO getDishVOById(Long id) {
        DishVO dishVO = dishMapper.getDishVOById(id);
        if (dishVO == null) {
            throw new BusinessException("菜品ID不存在");
        }
        return dishVO;
    }

    @Override
    @Transactional
    public boolean updateDish(DishDTO dishDTO) {
        Dish dish = dishMapper.getDishById(dishDTO.getId());
        if (dish == null) {
            throw new BusinessException("菜品ID不存在");
        }

        // 当数据库中得到的菜品名称和传入的菜品名称不同时，需要判断菜品名称是否重复
        if (!dishDTO.getName().equals(dish.getName())) {
            if (dishMapper.getDishByDishName(dishDTO.getName()) != null) {
                throw new BusinessException("菜品名称重复");
            }
        }

        BeanUtils.copyProperties(dishDTO, dish);
        // 更新菜品
        int affectRow = dishMapper.updateDish(dish);

        // 更新口味数据
        List<DishFlavor> dishFlavors = dishDTO.getFlavors();
        // 先删除原有的数据，然后再插入
        dishFlavorMapper.deleteByDishIds(Collections.singletonList(dish.getId()));
        // 当dishFlavors非空时才进行插入
        if (!CollectionUtils.isEmpty(dishFlavors)) {
            dishFlavors.forEach(dishFlavor -> dishFlavor.setDishId(dishDTO.getId()));
            dishFlavorMapper.saveBatch(dishFlavors);
        }
        return affectRow > 0;
    }

    @Override
    public boolean updateDishStatus(Long id, Integer status) {
        Dish dish = dishMapper.getDishById(id);
        if (dish == null) {
            throw new BusinessException("菜品ID不存在");
        }
        // 如果菜品关联了套餐就不能停售
        List<Long> setMealWithDish = setMealDishMapper.getCountByDishIds(Collections.singletonList(id));
        if (setMealWithDish.size() != 0) {
            throw new BusinessException("修改状态失败，菜品ID为：" + setMealWithDish + " 存在关联套餐");
        }
        dish.setStatus(status);
        int affectRow = dishMapper.updateDish(dish);
        return affectRow > 0;
    }

    @Override
    public List<Dish> getDishListByCategoryId(Long categoryId) {
        return dishMapper.getDishListByCategoryId(categoryId);
    }
}
