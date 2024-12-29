package com.example.canteen.repository;

import com.example.canteen.entity.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    List<User> findAllByStatusTrue();

    boolean existsByUsernameAndStatusTrue(@NotBlank String username);

    List<User> id(Long id);

}
