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
@Table(name = "tb_Stock")
public class Cart {
    @Id
    @Column(name = "Stock_ID")
    private int cartId;
    @Column(name = "Product_ID")
    private int productId;
    @Column(name = "Quantity")
    private int quantity;
}
