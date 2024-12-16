package com.example.canteen.repository;

import com.example.canteen.entity.Product;
import com.example.canteen.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;



public interface StockRepository extends JpaRepository<Stock, Long> {
    Stock findByProductId(Long id);
    boolean existsByProduct(Product product);
}
