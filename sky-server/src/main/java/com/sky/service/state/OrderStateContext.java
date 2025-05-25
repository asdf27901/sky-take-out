package com.sky.service.state;

import com.sky.entity.Orders;
import com.sky.enums.OrderEvent;
import com.sky.enums.OrderStatus;
import com.sky.exception.OrderBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Service;

@Service
public class OrderStateContext {
    private Orders order;
    private IOrderState<OrderStatus, OrderEvent> iOrderState;
    private StateMachine<OrderStatus, OrderEvent> stateMachine;

    @Autowired
    private PendingPaymentState pendingPaymentState;
    @Autowired
    private ToBeConfirmedState toBeConfirmedState;

    public void init(Orders order, StateMachine<OrderStatus, OrderEvent> stateMachine) {
        this.order = order;
        this.stateMachine = stateMachine;
        OrderStatus orderStatus = OrderStatus.fromState(order.getStatus());
        switch (orderStatus) {
            case PENDING_PAYMENT: // 待付款状态
                this.iOrderState = pendingPaymentState;  // 需要注意，如果是自己new的话，对象中的依赖注入全部都会失效
                break;
            case TO_BE_CONFIRMED: // 待接单状态
                this.iOrderState = toBeConfirmedState;
                break;
            default:
                throw new OrderBusinessException("不存在的订单状态");
        }
    }

    public void pay() {
        iOrderState.pay(order, stateMachine);
    }

    public void userCancel() {
        iOrderState.userCancel(order, stateMachine);
    }
}
