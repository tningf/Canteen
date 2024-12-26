package com.example.canteen.service;

import com.example.canteen.dto.OrderDto;
import com.example.canteen.entity.*;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.enums.OrderStatus;
import com.example.canteen.exception.AppException;
import com.example.canteen.mapper.OrderMapper;
import com.example.canteen.repository.OrderRepository;
import com.example.canteen.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final StockRepository stockRepository;
    private final CartService cartService;
    private final OrderMapper orderMapper;
    private final UserContextService userContextService;

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Transactional
    public Order placeOrder(Long patientId) {
        Cart cart = cartService.getCartByPatientId(patientId);

        validateStockAvailability(cart);

        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalPrice(orderItemList));


        if (order.getTotalAmount().compareTo(cart.getPatient().getPatientBalance().getBalance()) > 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private void validateStockAvailability(Cart cart) {
        for (CartItem cartItem : cart.getItems()) {
            Stock stock = cartItem.getProduct().getStock();
            if (stock.getQuantity() < cartItem.getQuantity()) {
                throw new AppException(
                        ErrorCode.INSUFFICIENT_STOCK
                );
            }
        }
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setPatient(cart.getPatient());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
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
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    public List<OrderDto> getPatientOrders(Long patientId) {
        List<Order> orders = orderRepository.findByPatientId(patientId);
        return orders.stream()
                .map(orderMapper::convertToDto)
                .toList();
    }

    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::convertToDto)
                .toList();
    }

    @Transactional
    public OrderDto confirmOrder(Long orderId) {
        try {
            String currentUser = userContextService.getCurrentUser();
            log.info("Starting order confirmation process. Order ID: {}, User: {}",
                    orderId, currentUser);

            Order order = getOrderById(orderId);
            orderValidator.validateConfirmation(order);

            updateStockForConfirmation(order);

            // Subtract total amount from patient balance
            BigDecimal totalAmount = order.getTotalAmount();
            Patient patient = order.getPatient();
            BigDecimal newBalance = patient.getPatientBalance().getBalance().subtract(totalAmount);
            patient.getPatientBalance().setBalance(newBalance);

            order.setOrderStatus(OrderStatus.CONFIRMED);
            order.setConfirmedAt(LocalDateTime.now());
            order.setConfirmedBy(currentUser);

            Order confirmedOrder = orderRepository.save(order);
            log.info("Order confirmed successfully. Order ID: {}", orderId);

            return orderMapper.convertToDto(confirmedOrder);
        } catch (Exception e) {
            log.error("Error confirming order: {}", orderId, e);
            throw e;
        }
    }

    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        try {
            String currentUser = userContextService.getCurrentUser();

            Order order = getOrderById(orderId);
            orderValidator.validateCancellation(order);

            if (order.getOrderStatus() == OrderStatus.CONFIRMED) {
                restorePaymentForCancellation(order);
                restoreStockForCancellation(order);
            }

            order.setOrderStatus(OrderStatus.CANCELED);
            order.setCanceledAt(LocalDateTime.now());
            order.setCanceledBy(currentUser);

            Order canceledOrder = orderRepository.save(order);

            return orderMapper.convertToDto(canceledOrder);
        } catch (Exception e) {
            log.error("Error canceling order: {}", orderId, e);
            throw e;
        }
    }

    private void restorePaymentForCancellation(Order order) {
        BigDecimal totalAmount = order.getTotalAmount();
        Patient patient = order.getPatient();
        BigDecimal newBalance = patient.getPatientBalance().getBalance().add(totalAmount);
        patient.getPatientBalance().setBalance(newBalance);
    }

    private void updateStockForConfirmation(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            Stock stock = orderItem.getProduct().getStock();
            int newQuantity = stock.getQuantity() - orderItem.getQuantity();
            stock.setQuantity(newQuantity);
            stockRepository.save(stock);
        }
    }

    private void restoreStockForCancellation(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            Stock stock = orderItem.getProduct().getStock();
            int newQuantity = stock.getQuantity() + orderItem.getQuantity();
            stock.setQuantity(newQuantity);
            stockRepository.save(stock);
        }
    }
}
