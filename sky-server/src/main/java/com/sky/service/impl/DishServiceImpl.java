package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.BusinessException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

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
}
