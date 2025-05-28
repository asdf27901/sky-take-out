package com.sky.service;

import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;

public interface WorkSpaceService {
    OrderOverViewVO getOverviewOrders();

    DishOverViewVO getOverviewDishes();
}
