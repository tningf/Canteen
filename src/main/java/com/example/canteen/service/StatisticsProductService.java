package com.example.canteen.service;

import com.example.canteen.dto.SaleProductStatistics;
import com.example.canteen.entity.Order;
import com.example.canteen.enums.OrderStatus;
import com.example.canteen.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsProductService {
    private final OrderRepository orderRepository;

    public List<SaleProductStatistics> generateSaleProductStatistics(LocalDate startDate, LocalDate endDate, String categoryName) {
        List<Order> orders = orderRepository.findByOrderStatus(OrderStatus.CONFIRMED);

        // Filter orders by date range if provided
        if (startDate != null && endDate != null) {
            orders = orders.stream()
                    .filter(order -> {
                        LocalDate orderDate = order.getOrderDate().toLocalDate();
                        return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                    })
                    .toList();
        }

        Map<String, SaleProductStatistics.SaleProductStatisticsBuilder> salesByProduct = new HashMap<>();

        orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem -> categoryName == null ||
                        orderItem.getProduct().getCategory().getName().equalsIgnoreCase(categoryName))
                .forEach(orderItem -> {
                    String productKey = orderItem.getProduct().getId().toString();

                    salesByProduct.computeIfAbsent(productKey, k -> SaleProductStatistics.builder()
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

        return salesByProduct.values().stream()
                .map(SaleProductStatistics.SaleProductStatisticsBuilder::build)
                .sorted(Comparator.comparing(SaleProductStatistics::getProductName))
                .map(dto -> {
                    int index = salesByProduct.values().stream()
                            .map(SaleProductStatistics.SaleProductStatisticsBuilder::build)
                            .sorted(Comparator.comparing(SaleProductStatistics::getProductName))
                            .toList()
                            .indexOf(dto) + 1;
                    return SaleProductStatistics.builder()
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
