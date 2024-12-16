package com.example.canteen.repository;

import com.example.canteen.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByPatientId(Long patientId);
}
