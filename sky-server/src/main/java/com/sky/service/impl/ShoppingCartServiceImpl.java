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
import java.util.List;

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

        ShoppingCart cartItem = getShoppingCartItem(currentId, shoppingCartDTO);
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        if (cartItem != null) {
            // 如果找到的购物车数据不为空，那么直接在原有数量上+1
            cartItem.setNumber(cartItem.getNumber() + 1);
            int affectRow = shoppingCartMapper.updateCartItem(cartItem);
            return affectRow > 0;
        }
        // 如果找不到购物车数据，说明是第一次添加到购物车
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

    @Override
    public List<ShoppingCart> listShoppingCart() {
        return shoppingCartMapper.listShoppingCart(BaseContext.getCurrentId());
    }

    @Override
    public boolean clearShoppingCart() {
        int affectRows = shoppingCartMapper.clearShoppingCart(BaseContext.getCurrentId());
        return affectRows >= 0;
    }

    @Override
    public boolean subShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        Long currentId = BaseContext.getCurrentId();
        ShoppingCart cartItem = getShoppingCartItem(currentId, shoppingCartDTO);
        if (cartItem == null) {
            // 购物车没有找到对应的商品数据
            if (shoppingCartDTO.getDishId() != null) {
                throw new BusinessException("没有找到对应的菜品ID：" + shoppingCartDTO.getDishId());
            }
            throw new BusinessException("没有找到对应的套餐ID：" + shoppingCartDTO.getSetmealId());
        }
        Integer number = cartItem.getNumber();
        int affectRow;
        if (number == 1) {
            // 如果只剩下1个，那么直接删除对应的购物车数据
            affectRow = shoppingCartMapper.deleteCartItem(cartItem.getId());
        }else {
            // 更新商品数量
            cartItem.setNumber(cartItem.getNumber() - 1);
            affectRow = shoppingCartMapper.updateCartItem(cartItem);
        }
        return affectRow > 0;
    }

    @Override
    public ShoppingCart getShoppingCartItem(Long userId, ShoppingCartDTO shoppingCartDTO) {
        Long dishId = shoppingCartDTO.getDishId();
        Long setmealId = shoppingCartDTO.getSetmealId();
        if ((dishId == null && setmealId == null) || (dishId != null && setmealId != null)) {
            // 业务判断，两个id都为空时无法添加到购物车中
            throw new BusinessException("参数错误，别乱搞~");
        }
        return shoppingCartMapper.getShoppingCartItem(userId, shoppingCartDTO);
    }
}
