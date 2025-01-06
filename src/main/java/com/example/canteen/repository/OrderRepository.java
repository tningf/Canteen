package com.example.canteen.repository;

import com.example.canteen.entity.Order;
import com.example.canteen.entity.Patient;
import com.example.canteen.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByPatientId(Long patientId);

    List<Order> findByOrderStatusAndOrderDateBetween(OrderStatus orderStatus, LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByOrderStatus(OrderStatus orderStatus);

    @Query(value = "SELECT DISTINCT o FROM Order o " +
            "LEFT JOIN FETCH o.orderItems oi " +
            "LEFT JOIN FETCH oi.product p " +
            "LEFT JOIN FETCH o.patient " +
            "WHERE o.orderStatus = :status",
            countQuery = "SELECT COUNT(o) FROM Order o WHERE o.orderStatus = :status")
    Page<Order> findAllByStatusWithDetails(@Param("status") OrderStatus status, Pageable pageable);

}
