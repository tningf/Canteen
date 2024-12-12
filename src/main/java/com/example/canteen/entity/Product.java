package com.example.canteen.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_Product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Product_ID")
    private Long id;

    @Column(name = "ProductName",columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "Unit")
    private int unit;

    @Column(name = "SellPrice")
    private double price;

    @Column(name = "STATUS")
    private boolean status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Category_ID")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    public Product(String name, int unit, double price, boolean status, Category category) {
        this.name = name;
        this.unit = unit;
        this.price = price;
        this.status = status;
        this.category = category;
    }

}
