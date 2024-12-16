package com.example.canteen.service;

import com.example.canteen.dto.OrderDto;
import com.example.canteen.entity.*;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.enums.OrderStatus;
import com.example.canteen.exception.AppExeception;
import com.example.canteen.mapper.OrderMapper;
import com.example.canteen.repository.OrderRepository;
import com.example.canteen.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final StockRepository stockRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;

    public Order placeOrder(Long patientId) {
        Cart cart = cartService.getCartByPatientId(patientId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalPrice(orderItemList));
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setPatient(cart.getPatient());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            Stock stock = product.getStock();
            stock.setQuantity(stock.getQuantity() - cartItem.getQuantity());
            stockRepository.save(stock);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }

    private BigDecimal calculateTotalPrice(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(orderItem -> orderItem.getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::convertToDto)
                .orElseThrow(() -> new AppExeception(ErrorCode.ORDER_NOT_FOUND));
    }

    public List<OrderDto> getPatientOrders(Long patientId) {
        List<Order> orders = orderRepository.findByPatientId(patientId);
        return orders.stream()
                .map(orderMapper::convertToDto)
                .toList();
    }

}
