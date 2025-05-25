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

import java.time.LocalDateTime;

@Service
@Slf4j
public class CompleteState implements IOrderState<OrderStatus, OrderEvent> {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public void pay(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已完成支付，不要重复付款");
    }

    @Override
    public void userCancel(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("请联系商家进行退款");
    }

    @Override
    public void confirmOrder(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已完成，不要重复接单");
    }

    @Override
    public void adminCancel(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine, String cancelReason) {
        // 商家的退款订单操作
        Message<OrderEvent> event = MessageBuilder.withPayload(OrderEvent.ADMIN_CANCEL)
                .setHeader("order", order).build();
        boolean accepted = stateMachine.sendEvent(event);
        if (accepted) {
            order.setStatus(stateMachine.getState().getId().getState());
            order.setPayStatus(Orders.REFUND);
            order.setCancelReason(cancelReason);
            order.setCancelTime(LocalDateTime.now());
            orderMapper.update(order);
            log.info("{}取消成功", order.getNumber());
        }
    }

    @Override
    public void delivery(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已完成，不要重复操作");
    }

    @Override
    public void complete(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        throw new OrderBusinessException("订单已完成，不要重复操作");
    }
}
