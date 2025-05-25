package com.sky.service.state;

import com.sky.entity.Orders;
import com.sky.enums.OrderEvent;
import com.sky.enums.OrderStatus;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PendingPaymentState implements IOrderState<OrderStatus, OrderEvent>{

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void pay(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        Message<OrderEvent> event = MessageBuilder.withPayload(OrderEvent.PAY)
                .setHeader("order", order).build();

        boolean accepted = stateMachine.sendEvent(event);
        if (accepted) {
            order.setStatus(stateMachine.getState().getId().getState());
            order.setPayStatus(Orders.PAID);
            order.setCheckoutTime(LocalDateTime.now());
            orderMapper.update(order);
            log.info("{}订单支付成功", order.getNumber());
        }
    }

    @Override
    public void cancel(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        // 待付款状态下取消订单
        // 直接修改订单状态为已取消即可
        Message<OrderEvent> event = MessageBuilder.withPayload(OrderEvent.CANCEL)
                .setHeader("order", order).build();

        boolean accepted = stateMachine.sendEvent(event);
        if (accepted) {
            order.setStatus(stateMachine.getState().getId().getState());
            orderMapper.update(order);
            log.info("{}取消成功", order.getNumber());
        }
    }
}
