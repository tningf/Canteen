package com.example.canteen.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_Transaction_History")
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Transaction_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "Patient_ID", nullable = false)
    private Patient patient;

    @Column(name = "TransactionType", columnDefinition = "NVARCHAR(50)")
    private String transactionType;

    @Column(name = "TransactionAmount", nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "TransactionDate", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime transactionDate;

    @Column(name = "Remarks", columnDefinition = "NVARCHAR(255)")
    private String remarks; // Optional comments or descriptions
}
