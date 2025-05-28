package com.sky.service.impl;

import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public OrderOverViewVO getOverviewOrders() {

        return orderMapper.getAllStatusOrderCount();
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        return dishMapper.getAllStatusDishesCount();
    }
}
