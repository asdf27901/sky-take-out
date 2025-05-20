package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.ShoppingCart;
import com.sky.exception.BusinessException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public boolean addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart cartItem = shoppingCartMapper.getShoppingCartItem(currentId, shoppingCartDTO);
        if (cartItem != null) {
            // 如果找到的购物车数据不为空，那么直接在原有数量上+1
            cartItem.setNumber(cartItem.getNumber() + 1);
            int affectRow = shoppingCartMapper.updateCartItem(cartItem);
            return affectRow > 0;
        }
        // 如果找不到购物车数据，说明是第一次添加到购物车
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        if ((dishId == null && setmealId == null) || (dishId != null && setmealId != null)) {
            // 业务判断，两个id都为空时无法添加到购物车中
            throw new BusinessException("参数错误，别乱搞~");
        }
        if (dishId != null) {
            // 说明添加菜品到购物车中
            Dish dish = dishMapper.getDishById(dishId);
            if (dish == null) {
                throw new BusinessException("菜品id不存在：" + dishId);
            }
            cartItem = ShoppingCart.builder()
                    .dishId(dish.getId())
                    .image(dish.getImage())
                    .name(dish.getName())
                    .dishFlavor(shoppingCartDTO.getDishFlavor())
                    .amount(dish.getPrice())
                    .build();
        }else {
            // 说明添加套餐到购物车中
            SetmealVO setmealVO = setMealMapper.getSetMealById(setmealId);
            if (setmealVO == null) {
                throw new BusinessException("套餐ID不存在：" + setmealId);
            }
            cartItem = ShoppingCart.builder()
                    .setmealId(setmealVO.getId())
                    .image(setmealVO.getImage())
                    .name(setmealVO.getName())
                    .amount(setmealVO.getPrice())
                    .build();
        }
        cartItem.setNumber(1);
        cartItem.setUserId(currentId);
        cartItem.setCreateTime(LocalDateTime.now());
        int affectRow = shoppingCartMapper.saveItem(cartItem);

        return affectRow > 0;
    }
}
