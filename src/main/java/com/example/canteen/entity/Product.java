package com.example.canteen.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_ID")
    private Long productId;

    @Column(name = "ProductName")
    private String productName;

    @Column(name = "Unit")
    private int unit;

    @Column(name = "SellPrice")
    private double sellPrice;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "Category_ID")
    private String categoryId;

    // No-args constructor
    public Product() {
    }

    // Constructor với tham số đầy đủ
    public Product(Long productId, String productName, int unit, double sellPrice, Boolean status, String categoryId) {
        this.productId = productId;
        this.productName = productName;
        this.unit = unit;
        this.sellPrice = sellPrice;
        this.status = status;
        this.categoryId = categoryId;
    }


    // Getters và Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
