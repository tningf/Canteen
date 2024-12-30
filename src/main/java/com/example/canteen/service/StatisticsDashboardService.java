package com.example.canteen.service;

import com.example.canteen.dto.statistics.OrderStatistics;
import com.example.canteen.dto.statistics.TopSellingProduct;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsDashboardService {
    private final OrderRepository orderRepository;
    private static final int DEFAULT_TOP_PRODUCTS_LIMIT = 5;

    public OrderStatistics getOrderStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        validateDateRange(startDate, endDate);

        List<Order> ordersInRange = orderRepository.findByOrderStatusAndOrderDateBetween(
                OrderStatus.CONFIRMED,
                startDate,
                endDate
        );
        log.info("Start date: {}, End date: {}", startDate, endDate);
        return generateOrderStatistics(ordersInRange);
    }

    public OrderStatistics getDefaultStatistics() {
        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);
        return generateOrderStatistics(orders);
    }

    /**
     * Validates the provided date range.
     *
     * @param startDate Start date of the range.
     * @param endDate   End date of the range.
     */
    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null || endDate == null) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.INVALID_DATE_RANGE);
        }
//        if (endDate.isAfter(LocalDateTime.now())) {
//            throw new AppException(ErrorCode.FUTURE_DATE_NOT_ALLOWED);
//        }
    }

    /**
     * Generates OrderStatistics from a list of orders.
     *
     * @param orders List of orders to process.
     * @return Populated OrderStatistics object.
     */
    private OrderStatistics generateOrderStatistics(List<Order> orders) {
        int totalOrders = orders.size();

        // Calculate total products sold
        int totalProductsSold = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .mapToInt(OrderItem::getQuantity)
                .sum();

        // Calculate total revenue
        BigDecimal totalRevenue = orders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate top-selling products
        List<TopSellingProduct> topSellingProducts = orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .collect(Collectors.groupingBy(
                        item -> item.getProduct().getId(),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                items -> {
                                    String productName = items.getFirst().getProduct().getName();
                                    int totalQuantity = items.stream().mapToInt(OrderItem::getQuantity).sum();
                                    BigDecimal totalProductRevenue = items.stream()
                                            .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                                    return TopSellingProduct.builder()
                                            .productId(items.getFirst().getProduct().getId())
                                            .productName(productName)
                                            .quantitySold(totalQuantity)
                                            .revenue(totalProductRevenue)
                                            .build();
                                }
                        )
                ))
                .values().stream()
                .sorted(Comparator
                        .comparing(TopSellingProduct::getRevenue, Comparator.reverseOrder())
                        .thenComparing(TopSellingProduct::getQuantitySold, Comparator.reverseOrder()))
                .limit(DEFAULT_TOP_PRODUCTS_LIMIT)
                .toList();

        return OrderStatistics.builder()
                .totalOrders(totalOrders)
                .totalProductsSold(totalProductsSold)
                .totalRevenue(totalRevenue)
                .topSellingProducts(topSellingProducts)
                .build();
    }
}