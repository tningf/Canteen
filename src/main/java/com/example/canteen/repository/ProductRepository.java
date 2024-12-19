package com.example.canteen.repository;

import com.example.canteen.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

    List<Product> findAllByStatusTrue();

    Boolean existsByName(String name);
}
