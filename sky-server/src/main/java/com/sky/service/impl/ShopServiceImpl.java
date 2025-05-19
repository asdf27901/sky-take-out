package com.sky.service.impl;

import com.sky.constant.RedisConstant;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer getShopStatus() {
        return (Integer) redisTemplate.opsForValue().get(RedisConstant.SHOP_STATUS_KEY);
    }

    @Override
    public void setShopStatus(Integer status) {
        redisTemplate.opsForValue().set(RedisConstant.SHOP_STATUS_KEY, status);
    }
}
