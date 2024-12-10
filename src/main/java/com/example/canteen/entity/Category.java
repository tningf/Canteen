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
@Table(name = "tb_Category")
public class Category {
    @Id
    @Column(name = "Category_ID")
    private Integer categoryId;
    @Column(name = "CategoryName")
    private String categoryName;
    @Column(name = "Status")
    private Boolean status;

}
