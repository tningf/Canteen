package com.example.canteen.repository;

import com.example.canteen.entity.Order;
import com.example.canteen.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPatientId(Long patientId);

    List<Order> findByOrderStatusAndOrderDateBetween(OrderStatus orderStatus, LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByOrderStatus(OrderStatus orderStatus);
}
