package com.example.canteen.repository;

import com.example.canteen.entity.Role;
import com.example.canteen.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Collection<Role> findAllByName(RoleName name);
}