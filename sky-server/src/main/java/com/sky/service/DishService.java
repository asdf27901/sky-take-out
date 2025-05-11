package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

public interface DishService {
    PageResult<DishVO> getDishList(DishPageQueryDTO dishPageQueryDTO);

    boolean saveDish(DishDTO dishDTO);

    boolean deleteDishByIds(Long[] ids);
}
