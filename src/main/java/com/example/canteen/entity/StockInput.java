package com.example.canteen.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_StockInput")
public class StockInput {
    @Id
    @Column(name = "StockInput_ID")
    private Long stockInputId;
    @Column(name = "Product_quanatity")
    private int productQuantity;
    @Column(name = "Total_amount")
    private double totalAmount;
    @Column(name = "Supplier")
    private String supplier;
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
}
