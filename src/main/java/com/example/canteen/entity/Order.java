package com.example.canteen.entity;

import com.example.canteen.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_Orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Order_ID")
    private Long id;

    @Column(name = "Order_Date")
    private LocalDate orderDate;

    @Column(name = "Total_Amount")
    private BigDecimal totalAmount;

    @Column(name = "Order_Status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "Patient_ID")
    private Patient patient;

//    @ManyToOne
//    @JoinColumn(name = "Status_Updated_By")
//    private User statusUpdatedBy;
}