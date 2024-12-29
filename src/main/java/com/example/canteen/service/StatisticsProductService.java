package com.example.canteen.service;

import com.example.canteen.dto.statistics.*;
import com.example.canteen.entity.*;
import com.example.canteen.enums.OrderStatus;
import com.example.canteen.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsProductService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;

    public List<SaleProductStatistics> generateSaleProductStatistics(LocalDate startDate, LocalDate endDate, String categoryName) {
        List<Order> orders = filterOrdersByDate(orderRepository.findByOrderStatus(OrderStatus.CONFIRMED), startDate, endDate);

        AtomicInteger counter = new AtomicInteger(1);
        Map<String, SaleProductStatistics.SaleProductStatisticsBuilder> salesByProduct = new HashMap<>();
        orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .filter(orderItem -> categoryName == null || orderItem.getProduct().getCategory().getName().equalsIgnoreCase(categoryName))
                .forEach(orderItem -> {
                    String productKey = orderItem.getProduct().getId().toString();
                    salesByProduct.computeIfAbsent(productKey, k -> SaleProductStatistics.builder()
                                    .sequenceNumber(counter.getAndIncrement())
                            .productName(orderItem.getProduct().getName())
                            .categoryName(orderItem.getProduct().getCategory().getName())
                            .unit(orderItem.getProduct().getUnit())
                            .unitPrice(orderItem.getPrice())
                            .quantity(0)
                            .totalPrice(BigDecimal.ZERO))
                            .quantity(salesByProduct.get(productKey).build().getQuantity() + orderItem.getQuantity())
                            .totalPrice(salesByProduct.get(productKey).build().getTotalPrice()
                                    .add(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))));
                });

        return salesByProduct.values().stream()
                .map(SaleProductStatistics.SaleProductStatisticsBuilder::build)
                .sorted(Comparator.comparing(SaleProductStatistics::getSequenceNumber)
                        .thenComparing(SaleProductStatistics::getProductName))
                .collect(Collectors.toList());
    }

    public List<StaffSaleStatistics> generateStaffSaleStatistics(LocalDate startDate, LocalDate endDate, String username) {
        List<Order> orders = filterOrdersByDate(orderRepository.findByOrderStatus(OrderStatus.CONFIRMED), startDate, endDate);
        if (username != null && !username.trim().isEmpty()) {
            orders = orders.stream().filter(order -> username.equalsIgnoreCase(order.getConfirmedBy())).toList();
        }

        Map<String, String> userDepartments = new HashMap<>();
        List<StaffSaleStatistics> statistics = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(1);

        orders.forEach(order -> {
            String department = userDepartments.computeIfAbsent(order.getConfirmedBy(), usernameDepartment -> {
                User user = userRepository.findByUsername(order.getConfirmedBy()).orElse(null);
                return user != null && user.getDepartments() != null && !user.getDepartments().isEmpty() ?
                        user.getDepartments().stream().map(Department::getDepartmentName).collect(Collectors.joining(", ")) : "";
            });

            order.getOrderItems().forEach(orderItem -> statistics.add(StaffSaleStatistics.builder()
                    .sequenceNumber(counter.getAndIncrement())
                    .patientId(String.valueOf(order.getPatient().getId()))
                    .cardNumber(order.getPatient().getCardNumber())
                    .fullName(order.getPatient().getFullName())
                    .product(orderItem.getProduct().getName())
                    .categoryName(orderItem.getProduct().getCategory().getName())
                    .unit(orderItem.getProduct().getUnit())
                    .price(orderItem.getPrice())
                    .department(department)
                    .quantity(orderItem.getQuantity())
                    .date(order.getOrderDate())
                    .user(order.getConfirmedBy())
                    .build()));
        });

        return statistics.stream()
                .sorted(Comparator.comparing(StaffSaleStatistics::getSequenceNumber).thenComparing(StaffSaleStatistics::getDate))
                .collect(Collectors.toList());
    }

    public List<PatientBuyStatistics> generatePatientBuyStatistics(LocalDate startDate, LocalDate endDate, String cardNumber) {
        List<Order> orders = filterOrdersByDate(orderRepository.findByOrderStatus(OrderStatus.CONFIRMED), startDate, endDate);
        if (cardNumber != null && !cardNumber.trim().isEmpty()) {
            orders = orders.stream().filter(order -> cardNumber.equals(order.getPatient().getCardNumber())).toList();
        }

        Map<String, String> patientDepartments = new HashMap<>();
        List<PatientBuyStatistics> statistics = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(1);

        orders.forEach(order -> {
            String department = patientDepartments.computeIfAbsent(order.getPatient().getCardNumber(), patientDepartment -> {
                Patient patient = patientRepository.findByCardNumber(order.getPatient().getCardNumber()).orElse(null);
                return patient != null && patient.getDepartments() != null && !patient.getDepartments().isEmpty() ?
                        patient.getDepartments().stream().map(Department::getDepartmentName).collect(Collectors.joining(", ")) : "";
            });

            order.getOrderItems().forEach(orderItem -> statistics.add(PatientBuyStatistics.builder()
                    .sequenceNumber(counter.getAndIncrement())
                    .cardNumber(order.getPatient().getCardNumber())
                    .product(orderItem.getProduct().getName())
                    .categoryName(orderItem.getProduct().getCategory().getName())
                    .unit(orderItem.getProduct().getUnit())
                    .price(orderItem.getPrice())
                    .department(department)
                    .quantity(orderItem.getQuantity())
                    .totalPrice(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                    .build()));
        });

        return statistics.stream()
                .sorted(Comparator.comparing(PatientBuyStatistics::getCardNumber).thenComparing(PatientBuyStatistics::getSequenceNumber))
                .collect(Collectors.toList());
    }

    public List<DepartmentSaleStatistics> generateDepartmentSaleStatistics(LocalDate startDate, LocalDate endDate, String departmentName) {
        List<Order> orders = filterOrdersByDate(orderRepository.findByOrderStatus(OrderStatus.CONFIRMED), startDate, endDate);

        Map<String, String> userDepartments = new HashMap<>();
        List<DepartmentSaleStatistics> statistics = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(1);

        orders.forEach(order -> {
            String departments = userDepartments.computeIfAbsent(order.getConfirmedBy(), usernameDepartment -> {
                User user = userRepository.findByUsername(order.getConfirmedBy()).orElse(null);
                return user != null && user.getDepartments() != null && !user.getDepartments().isEmpty() ?
                        user.getDepartments().stream().map(Department::getDepartmentName).collect(Collectors.joining(", ")) : "";
            });

            Arrays.stream(departments.split(", "))
                    .filter(dept -> departmentName == null || departmentName.trim().isEmpty() || dept.equalsIgnoreCase(departmentName))
                    .forEach(dept -> order.getOrderItems().forEach(orderItem -> statistics.add(DepartmentSaleStatistics.builder()
                            .sequenceNumber(counter.getAndIncrement())
                            .departmentName(dept)
                            .product(orderItem.getProduct().getName())
                            .categoryName(orderItem.getProduct().getCategory().getName())
                            .unit(orderItem.getProduct().getUnit())
                            .price(orderItem.getPrice())
                            .quantity(orderItem.getQuantity())
                            .totalPrice(orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity())))
                            .user(order.getConfirmedBy())
                            .orderDate(order.getOrderDate())
                            .build())));
        });

        return statistics.stream()
                .sorted(Comparator.comparing(DepartmentSaleStatistics::getSequenceNumber)
                        .thenComparing(DepartmentSaleStatistics::getOrderDate)
                        .thenComparing(DepartmentSaleStatistics::getDepartmentName))
                .collect(Collectors.toList());
    }

    private List<Order> filterOrdersByDate(List<Order> orders, LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            return orders.stream()
                    .filter(order -> {
                        LocalDate orderDate = order.getOrderDate().toLocalDate();
                        return !orderDate.isBefore(startDate) && !orderDate.isAfter(endDate);
                    })
                    .collect(Collectors.toList());
        }
        return orders;
    }
}