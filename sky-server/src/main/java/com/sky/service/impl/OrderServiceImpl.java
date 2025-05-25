package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.context.BaseContext;
import com.sky.dto.*;
import com.sky.entity.*;
import com.sky.enums.OrderEvent;
import com.sky.enums.OrderStatus;
import com.sky.exception.BusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.service.state.OrderStateContext;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    private StateMachineFactory<OrderStatus, OrderEvent> stateMachineFactory;

    @Autowired
    private OrderStateContext orderStateContext;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

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

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    public void payment(OrdersPaymentDTO ordersPaymentDTO) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        Orders order = orderMapper.getOrderByOrderNumber(userId, ordersPaymentDTO.getOrderNumber());
        if (order == null) {
            throw new OrderBusinessException("订单不存在");
        }

        // 查到订单需要判断当前的订单状态是否是待支付订单, 通过状态机进行判断
        // 获取状态机
        StateMachine<OrderStatus, OrderEvent> stateMachine = buildOrderStateMachine(order);

        orderStateContext.init(order, stateMachine);
        orderStateContext.pay();

        // 由于没有商户资质，以下方法全部舍弃，直接调用支付成功的方法
//        User user = userMapper.getById(userId);
//
//        //调用微信支付接口，生成预支付交易单
//        JSONObject jsonObject = weChatPayUtil.pay(
//                ordersPaymentDTO.getOrderNumber(), //商户订单号
//                new BigDecimal(0.01), //支付金额，单位 元
//                "苍穹外卖订单", //商品描述
//                user.getOpenid() //微信用户的openid
//        );
//
//        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
//            throw new OrderBusinessException("该订单已支付");
//        }
//
//        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
//        vo.setPackageStr(jsonObject.getString("package"));
    }

    @Override
    public PageResult<OrderVO> getHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO) {
        Page<OrderVO> orderVOPage = PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize())
                .doSelectPage(() -> orderMapper.getHistoryOrders(ordersPageQueryDTO));
        return new PageResult<>(orderVOPage.getTotal(), orderVOPage.getResult(), ordersPageQueryDTO.getPage(), orderVOPage.getPageNum());
    }

    @Override
    public OrderVO getUserOrderDetail(Long id) {
        Long userId = BaseContext.getCurrentId();
        Orders order = orderMapper.getOrderByOrderIdAndUserId(userId, id);
        if (order == null) {
            throw new OrderBusinessException("订单不存在");
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.getOrderDetailByOrderId(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    @Override
    public void repeatOrder(Long id) {
        List<OrderDetail> orderDetailList = orderDetailMapper.getOrderDetailByOrderId(id);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new OrderBusinessException("订单不存在");
        }

        // 如果存在则重新加入购物车，同时需要判断加入购物车的菜品或者套餐是否存在或者已经停售
        List<Long> dishIdList = new ArrayList<>();
        List<Long> setmealIdList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            Long dishId = orderDetail.getDishId();
            if (dishId != null) {
                dishIdList.add(dishId);
            } else {
                setmealIdList.add(orderDetail.getSetmealId());
            }
        }
        List<Long> sellingDishIds = new ArrayList<>();
        List<Long> sellingSetMealIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dishIdList)) {  // 如果菜品数量本来就是0，就没必要去查数据库了
            sellingDishIds = dishMapper.getSellingDishListByIds(dishIdList);
        }
        if (!CollectionUtils.isEmpty(setmealIdList)) {  // 如果套餐数量本来就是0，就没必要去查数据库了
            sellingSetMealIds = setMealMapper.getSellingSetMealByIds(setmealIdList);
        }

        if (sellingDishIds.size() != dishIdList.size() || setmealIdList.size() != sellingSetMealIds.size()) {
            throw new OrderBusinessException("存在菜品或套餐下架，无法重新创建购物车");
        }

        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart);
            shoppingCart.setId(null);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setUserId(BaseContext.getCurrentId());
            shoppingCartList.add(shoppingCart);
        }
        shoppingCartMapper.saveItemBatch(shoppingCartList);
    }

    @Override
    public void userCancelOrder(Long id) {
        // 涉及到状态流转，所以需要使用状态机
        Long userId = BaseContext.getCurrentId();
        Orders order = orderMapper.getOrderByOrderIdAndUserId(userId, id);
        if (order == null) {
            throw new OrderBusinessException("订单不存在");
        }

//        Integer orderStatus = order.getStatus();
//        if (
//                !Objects.equals(orderStatus, Orders.PENDING_PAYMENT)
//                && !Objects.equals(orderStatus, Orders.TO_BE_CONFIRMED)
//                && !Objects.equals(orderStatus, Orders.CANCELLED)
//        ) {
//            throw new OrderBusinessException("当前订单状态不支持用户取消");
//        }
        // 创建状态机对象
        StateMachine<OrderStatus, OrderEvent> stateMachine = buildOrderStateMachine(order);
        orderStateContext.init(order, stateMachine);
        orderStateContext.userCancel();  // 区分是后台取消还是用户取消，就不需要判断状态了
    }

    @Override
    public PageResult<OrderVO> getOrderListByCondition(OrdersPageQueryDTO ordersPageQueryDTO) {
        Page<OrderVO> orderVOPage = PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize())
                .doSelectPage(() -> orderMapper.getHistoryOrders(ordersPageQueryDTO));

        // 需要添加菜品信息
        List<OrderVO> voList = orderVOPage.getResult();
        voList.forEach(orderVO -> {
            List<OrderDetail> orderDetailList = orderVO.getOrderDetailList();
            StringBuilder orderDishes = new StringBuilder();
            for (OrderDetail detail : orderDetailList) {
                orderDishes.append(detail.getName())
                        .append("*")
                        .append(detail.getNumber())
                        .append(";");
            }
            orderVO.setOrderDishes(orderDishes.toString());
        });

        return new PageResult<>(orderVOPage.getTotal(), orderVOPage.getResult(), orderVOPage.getPageSize(), orderVOPage.getPageNum());
    }

    @Override
    public OrderVO getOrderDetail(Long id) {
        Orders order = orderMapper.getOrderByOrderId(id);
        if (order == null) {
            throw new OrderBusinessException("订单不存在");
        }
        List<OrderDetail> orderDetailList = orderDetailMapper.getOrderDetailByOrderId(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        orderVO.setOrderDetailList(orderDetailList);
        return orderVO;
    }

    @Override
    public void confirmOrder(Long id) {
        Orders order = orderMapper.getOrderByOrderId(id);
        if (order == null) {
            throw new OrderBusinessException("订单不存在");
        }
        // 创建状态机对象
        StateMachine<OrderStatus, OrderEvent> stateMachine = buildOrderStateMachine(order);
        orderStateContext.init(order, stateMachine);
        orderStateContext.confirmOrder();
    }

    @Override
    public OrderStatisticsVO statistics() {
        return orderMapper.statistics();
    }

    @Override
    public void rejectOrder(OrdersRejectionDTO ordersRejectionDTO) {
        Orders order = orderMapper.getOrderByOrderId(ordersRejectionDTO.getId());
        if (order == null) {
            throw new OrderBusinessException("订单不存在");
        }
        // 创建状态机对象
        StateMachine<OrderStatus, OrderEvent> stateMachine = buildOrderStateMachine(order);
        orderStateContext.init(order, stateMachine);
        orderStateContext.adminCancel(ordersRejectionDTO.getRejectionReason());
    }

    @Override
    public void adminCancelOrder(OrdersCancelDTO ordersCancelDTO) {
        Orders order = orderMapper.getOrderByOrderId(ordersCancelDTO.getId());
        if (order == null) {
            throw new OrderBusinessException("订单不存在");
        }
        // 创建状态机对象
        StateMachine<OrderStatus, OrderEvent> stateMachine = buildOrderStateMachine(order);
        orderStateContext.init(order, stateMachine);
        orderStateContext.adminCancel(ordersCancelDTO.getCancelReason());
    }

    private StateMachine<OrderStatus, OrderEvent> buildOrderStateMachine(Orders order) {
        // 获取新的状态机实例
        StateMachine<OrderStatus, OrderEvent> stateMachine = stateMachineFactory.getStateMachine(order.getNumber());
        // 先停止，重置状态为订单当前状态
        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor()
                .doWithAllRegions(access -> access.resetStateMachineReactively(
                        new DefaultStateMachineContext<>(OrderStatus.fromState(order.getStatus()), null, null, null)
                ).block());
        stateMachine.startReactively().block();
        return stateMachine;
    }

}
