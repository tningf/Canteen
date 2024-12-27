package com.example.canteen.service;

import com.example.canteen.dto.OrderStatisticsDto;
import com.example.canteen.dto.SaleItemStatisticsDto;
import com.example.canteen.entity.Order;
import com.example.canteen.entity.OrderItem;
import com.example.canteen.enums.ErrorCode;
import com.example.canteen.enums.OrderStatus;
import com.example.canteen.exception.AppException;
import com.example.canteen.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsService {
    private final OrderRepository orderRepository;

    public OrderStatisticsDto getOrderStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        validateDateRange(startDate, endDate);

        List<Order> ordersInRange = orderRepository.findByOrderStatusAndOrderDateBetween(
                OrderStatus.CONFIRMED,
                startDate,
                endDate
        );
        int totalOrders = ordersInRange.size();
        int totalProductsSold = calculateTotalProductsSold(ordersInRange);
        BigDecimal totalRevenue = calculateTotalRevenue(ordersInRange);
        return OrderStatisticsDto.builder()
                .totalOrders(totalOrders)
                .totalProductsSold(totalProductsSold)
                .totalRevenue(totalRevenue)
                .build();
    }

    public OrderStatisticsDto getDefaultStatistics() {
        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);
        int totalOrders = orders.size();
        int totalProductsSold = calculateTotalProductsSold(orders);
        BigDecimal totalRevenue = calculateTotalRevenue(orders);
        return OrderStatisticsDto.builder()
                .totalOrders(totalOrders)
                .totalProductsSold(totalProductsSold)
                .totalRevenue(totalRevenue)
                .build();
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        if (endDate.isAfter(LocalDateTime.now())) {
            throw new AppException(ErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }
    }

    private int calculateTotalProductsSold(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .mapToInt(OrderItem::getQuantity)
                .sum();
    }

    private BigDecimal calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public List<SaleItemStatisticsDto> generateDetailedSalesStatistics() {
        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);
        // Create a map to aggregate sales by product
        Map<String, SaleItemStatisticsDto.SaleItemStatisticsDtoBuilder> salesByProduct = new HashMap<>();

        // Process all orders and their items
        orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .forEach(orderItem -> {
                    String productKey = orderItem.getProduct().getId().toString();

                    salesByProduct.computeIfAbsent(productKey, k -> SaleItemStatisticsDto.builder()
                                    .productName(orderItem.getProduct().getName())
                                    .categoryName(orderItem.getProduct().getCategory().getName())
                                    .unit(orderItem.getProduct().getUnit())
                                    .unitPrice(orderItem.getPrice())
                                    .quantity(0)
                                    .totalPrice(BigDecimal.ZERO)
                            ).quantity(salesByProduct.get(productKey).build().getQuantity() + orderItem.getQuantity())
                            .totalPrice(salesByProduct.get(productKey).build().getTotalPrice()
                                    .add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))));
                });

        // Convert the map to a sorted list and add sequence numbers
        return salesByProduct.values().stream()
                .map(SaleItemStatisticsDto.SaleItemStatisticsDtoBuilder::build)
                .sorted(Comparator.comparing(SaleItemStatisticsDto::getProductName))
                .map(dto -> {
                    int index = salesByProduct.values().stream()
                            .map(SaleItemStatisticsDto.SaleItemStatisticsDtoBuilder::build)
                            .sorted(Comparator.comparing(SaleItemStatisticsDto::getProductName))
                            .toList()
                            .indexOf(dto) + 1;
                    return SaleItemStatisticsDto.builder()
                            .sequenceNumber(index)
                            .productName(dto.getProductName())
                            .categoryName(dto.getCategoryName())
                            .unit(dto.getUnit())
                            .unitPrice(dto.getUnitPrice())
                            .quantity(dto.getQuantity())
                            .totalPrice(dto.getTotalPrice())
                            .build();
                })
                .collect(Collectors.toList());
    }

}