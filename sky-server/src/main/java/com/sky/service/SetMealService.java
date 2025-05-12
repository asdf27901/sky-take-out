package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealService {
    boolean saveSetMeal(SetmealDTO setmealDTO);

    PageResult<SetmealVO> listAllSetMeal(SetmealPageQueryDTO setmealPageQueryDTO);

    SetmealVO getSetMealById(Long id);

    boolean updateSetMeal(SetmealDTO setmealDTO);

    boolean updateSetMealStatus(Long id, Integer status);

    boolean deleteSetMealByIds(List<Long> ids);
}
