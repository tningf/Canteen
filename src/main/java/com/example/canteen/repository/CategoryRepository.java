package com.example.canteen.repository;

import com.example.canteen.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    boolean existsByName(String name);

    List<Category> findAllByStatusTrue();
}
