package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

public interface SetMealService {
    boolean saveSetMeal(SetmealDTO setmealDTO);

    PageResult<SetmealVO> listAllSetMeal(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO getSetMealById(Long id);
}
