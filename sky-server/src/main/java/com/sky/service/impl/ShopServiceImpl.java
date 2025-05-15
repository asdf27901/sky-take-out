package com.sky.service.impl;

import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopServiceImpl implements ShopService {

    private static final String SHOP_STATUS_KEY = "sky:shop:status";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Integer getShopStatus() {
        return (Integer) redisTemplate.opsForValue().get(SHOP_STATUS_KEY);
    }

    @Override
    public void setShopStatus(Integer status) {
        redisTemplate.opsForValue().set(SHOP_STATUS_KEY, status);
    }
}
