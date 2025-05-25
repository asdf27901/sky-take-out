package com.sky.service.state;

import com.sky.entity.Orders;
import com.sky.enums.OrderEvent;
import com.sky.enums.OrderStatus;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ToBeConfirmedState implements IOrderState<OrderStatus, OrderEvent>{

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void pay(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已完成支付，不要重复付款");
    }

    @Override
    public void userCancel(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        // 待接单状态下取消订单
        // 由于已经付款完成，所以取消订单需要修改订单状态和支付状态
        Message<OrderEvent> event = MessageBuilder.withPayload(OrderEvent.CANCEL)
                .setHeader("order", order).build();

        boolean accepted = stateMachine.sendEvent(event);
        if (accepted) {
            order.setStatus(stateMachine.getState().getId().getState());
            order.setPayStatus(Orders.REFUND);
            order.setCancelReason("用户取消");
            orderMapper.update(order);
            log.info("{}取消成功", order.getNumber());
        }
    }

    @Override
    public void confirmOrder(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        Message<OrderEvent> event = MessageBuilder.withPayload(OrderEvent.CONFIRMED)
                .setHeader("order", order).build();
        boolean accepted = stateMachine.sendEvent(event);
        if (accepted) {
            order.setStatus(stateMachine.getState().getId().getState());
            orderMapper.update(order);
            log.info("{}已接单", order.getNumber());
        }
    }
}
