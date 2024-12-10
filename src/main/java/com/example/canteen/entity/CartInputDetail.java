package com.example.canteen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tb_StockInputDetail")
public class CartInputDetail {
    @Id
    @Column(name = "StockInputDetail_ID")
    private int cartInputDetailId;
    @Column(name = "StockInput_ID")
    private int cartInputId;
    @Column(name = "Product_ID")
    private int productId;
    @Column(name = "Quantity")
    private int quantity;
    @Column(name = "PriceInput")
    private int priceInput;
}
