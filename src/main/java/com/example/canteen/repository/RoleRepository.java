package com.example.canteen.repository;

import com.example.canteen.entity.role.Role;
import com.example.canteen.entity.role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}