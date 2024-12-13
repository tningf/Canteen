package com.example.canteen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tb_StockInputDetail")
public class StockInputDetail {
    @Id
    @Column(name = "StockInputDetail_ID")
    private Long stockInputDetailId;
    @Column(name = "StockInput_ID")
    private Long stockInputId;
    @Column(name = "Product_ID")
    private Long productId;
    @Column(name = "Quantity")
    private int quantity;
    @Column(name = "PriceInput")
    private double priceInput;
}
