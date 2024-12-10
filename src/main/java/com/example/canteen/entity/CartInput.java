package com.example.canteen.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "tb_StockInput")
public class CartInput {
    @Id
    @Column(name = "StockInput_ID")
    private int cartInputId;
    @Column(name = "Product_quanatity")
    private int productQuantity;
    @Column(name = "Total_amount")
    private int totalAmount;
    @Column(name = "Supplier")
    private String supplier;
    @Column(name = "CREATE_DATE")
    private LocalDateTime createDate;
    @Column(name = "UPDATE_DATE")
    private LocalDateTime updateDate;
}
