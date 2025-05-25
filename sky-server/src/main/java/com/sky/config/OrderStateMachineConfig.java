package com.sky.config;

import com.sky.enums.OrderEvent;
import com.sky.enums.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
@Slf4j
public class OrderStateMachineConfig extends EnumStateMachineConfigurerAdapter<OrderStatus, OrderEvent> {
    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEvent> states) throws Exception {
        states.withStates()
                .initial(OrderStatus.PENDING_PAYMENT)  // 初始化状态机状态
                .states(EnumSet.allOf(OrderStatus.class));  // 传入所有可能的枚举类型
    }

    @Override
    // 列举出所有可能的正确的流转情况，不正确的不要写入
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEvent> transitions) throws Exception {
        transitions.withExternal()
                .source(OrderStatus.PENDING_PAYMENT).target(OrderStatus.TO_BE_CONFIRMED).event(OrderEvent.PAY)  // 待付款 -> 待接单状态
                .and()
                .withExternal()
                .source(OrderStatus.PENDING_PAYMENT).target(OrderStatus.CANCELLED).event(OrderEvent.USER_CANCEL) // 待付款 -> 已取消状态
                .and()
                .withExternal()
                .source(OrderStatus.PENDING_PAYMENT).target(OrderStatus.CANCELLED).event(OrderEvent.ADMIN_CANCEL) // 待付款 -> 已取消状态(商家)
                .and()
                .withExternal()
                .source(OrderStatus.TO_BE_CONFIRMED).target(OrderStatus.CONFIRMED).event(OrderEvent.CONFIRMED) // 待接单 -> 已接单状态
                .and()
                .withExternal()
                .source(OrderStatus.TO_BE_CONFIRMED).target(OrderStatus.CANCELLED).event(OrderEvent.USER_CANCEL) // 待接单 -> 已取消状态
                .and()
                .withExternal()
                .source(OrderStatus.TO_BE_CONFIRMED).target(OrderStatus.CANCELLED).event(OrderEvent.ADMIN_CANCEL) // 待接单 -> 已取消状态（商家）
                .and()
                .withExternal()
                .source(OrderStatus.CONFIRMED).target(OrderStatus.DELIVERY_IN_PROGRESS).event(OrderEvent.DELIVERY) // 已接单 -> 派送中状态
                .and()
                .withExternal()
                .source(OrderStatus.CONFIRMED).target(OrderStatus.CANCELLED).event(OrderEvent.ADMIN_CANCEL) // 已接单 -> 已取消状态(商家)
                .and()
                .withExternal()
                .source(OrderStatus.DELIVERY_IN_PROGRESS).target(OrderStatus.COMPLETED).event(OrderEvent.RECEIVE) // 派送中 -> 已完成状态
                .and()
                .withExternal()
                .source(OrderStatus.DELIVERY_IN_PROGRESS).target(OrderStatus.CANCELLED).event(OrderEvent.ADMIN_CANCEL)  // 派送中 -> 已取消状态(商家)
                .and()
                .withExternal()
                .source(OrderStatus.COMPLETED).target(OrderStatus.CANCELLED).event(OrderEvent.ADMIN_CANCEL);  // 已完成 -> 已取消(商家)
    }

    @Override
    // 状态机时间监听
    public void configure(StateMachineConfigurationConfigurer<OrderStatus, OrderEvent> config) throws Exception {
        config.withConfiguration().listener(new StateMachineListenerAdapter<OrderStatus, OrderEvent>() {

            @Override
            public void eventNotAccepted(Message<OrderEvent> event) {
                // 当传入状态机不被接受的状态时触发
                log.error("被拒绝的状态：{}，当前触发事件：{}", event.getHeaders().get("order"), event.getPayload());
            }

            @Override
            public void stateChanged(State<OrderStatus, OrderEvent> from, State<OrderStatus, OrderEvent> to) {
                // 当状态机状态发生改变时触发
                log.info("state changed from " + (from == null ? "none" : from.getId()) + " to " + to.getId());
            }
        });
    }
}
