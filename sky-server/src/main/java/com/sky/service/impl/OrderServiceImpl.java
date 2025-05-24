package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.BusinessException;
import com.sky.mapper.AddressMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {

        Long userId = BaseContext.getCurrentId();
        // 根据下单的地址id获取收货地址
        AddressBook address = addressMapper.getAddressById(userId, ordersSubmitDTO.getAddressBookId());
        if (address == null) {
            throw new BusinessException("地址不存在");
        }

        // 判断用户的购物车数据是否为空
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.listShoppingCart(userId);
        if (CollectionUtils.isEmpty(shoppingCartList)) {
            throw new BusinessException("购物车为空，无法下单");
        }

        // 创建订单对象
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        // 设置收货人姓名
        orders.setConsignee(address.getConsignee());
        // 设置收货人号码
        orders.setPhone(address.getPhone());
        // 设置收货人地址
        orders.setAddress(address.getProvinceName() + address.getCityName() + address.getDistrictName() + address.getDetail());
        // 设置用户ID
        orders.setUserId(userId);
        // 设置订单号
        String orderNumber = createOrderNumber(userId);
        orders.setNumber(orderNumber);
        // 设置下单时间
        LocalDateTime orderTime = LocalDateTime.now();
        orders.setOrderTime(orderTime);
        // 设置订单总金额
        BigDecimal orderAmount = countAmount(shoppingCartList)
                .add(BigDecimal.valueOf(ordersSubmitDTO.getPackAmount()))
                .add(BigDecimal.valueOf(6));
        orders.setAmount(orderAmount);

        // 插入订单表中
        orderMapper.insert(orders);

        // 将购物车数据插入到订单明细表中
        insertOrderDetail(shoppingCartList, orders.getId());

        // 清空购物车
        shoppingCartMapper.clearShoppingCart(userId);

        // 组装OrderSubmitVO返回
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderAmount(orderAmount)
                .orderNumber(orderNumber)
                .orderTime(orderTime)
                .build();
    }

    private void insertOrderDetail(List<ShoppingCart> shoppingCartList, Long orderId) {
        List<OrderDetail> orderDetailList = shoppingCartList.stream().map(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailMapper.insertBatch(orderDetailList);
    }

    private String createOrderNumber(Long userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(new Date());
        int randomNum = new Random().nextInt(900) + 100; // 100-999三位随机数
        return timestamp + userId.toString() + randomNum;
    }

    private BigDecimal countAmount(List<ShoppingCart> shoppingCartList) {
        return shoppingCartList.stream()
                .reduce(new BigDecimal("0"),  // 初始值
                        (subtotal, cart) -> subtotal.add(cart.getAmount().multiply(BigDecimal.valueOf(cart.getNumber()))), // 累加函数
                        BigDecimal::add);  // combiner，串行流时不重要
    }

}
