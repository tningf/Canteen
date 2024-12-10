package com.example.canteen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_Product_Image")
public class ProductImage {
    @Id
    @Column(name = "Product_Image_ID")
    private int productImageId;
    @Column(name = "Product_ID")
    private String productId;
    @Column(name = "Image_URL")
    private String imageUrl;
}
