package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Override
    public PageResult<Dish> getDishList(DishPageQueryDTO dishPageQueryDTO) {
        Page<Dish> dishPage = PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize())
                .doSelectPage(() -> dishMapper.getDishList(dishPageQueryDTO));
        return new PageResult<>(dishPage.getTotal(), dishPage.getResult(), dishPage.getPageSize(), dishPage.getPageNum());
    }
}
