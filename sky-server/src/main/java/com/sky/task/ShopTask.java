package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class ShopTask {

    @Autowired
    private OrderMapper orderMapper;

    // 每一分钟查询数据库中存在的超时待付款的订单
    @Scheduled(cron = "0 * * * * ?")
    public void dealWithTimeoutOrder() {
        log.info("定时处理超时待付款的订单");
        List<Orders> orders = orderMapper.findTimeoutOrders();
        if (!CollectionUtils.isEmpty(orders)) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelTime(LocalDateTime.now());
                order.setCancelReason("支付超时，取消订单");
            }
            orderMapper.updateOrderBatch(orders);
        }
    }

    // 每天凌晨1点钟检查数据库中是否存在派送中的订单
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkDeliveringOrders() {
        log.info("定时处理派送中的订单");
        List<Orders> orders = orderMapper.existsOrdersWithStatus();
        if (!CollectionUtils.isEmpty(orders)) {
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                order.setDeliveryTime(LocalDateTime.now());
            }
            orderMapper.updateOrderBatch(orders);
        }
    }
}
