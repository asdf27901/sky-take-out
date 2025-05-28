package com.sky.service.impl;

import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public OrderOverViewVO getOverviewOrders() {

        return orderMapper.getAllStatusOrderCount();
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        return dishMapper.getAllStatusDishesCount();
    }

    @Override
    public SetmealOverViewVO getOverviewSetmeals() {
        return setMealMapper.getAllStatusSetMealCount();
    }

    @Override
    public BusinessDataVO getBusinessData() {
        return orderMapper.getBusinessData(null, null);
    }
}
