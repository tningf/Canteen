package com.example.canteen.repository;

import com.example.canteen.entity.Category;
import com.example.canteen.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategoryName(String category);

//    List<Product> findAllByStatusTrue();

    Boolean existsByName(String name);


    Page<Product> findAllByStatusTrue(Pageable pageable);

    Page<Product> findByCategoryNameAndStatusTrue(String category, Pageable pageable);
}
