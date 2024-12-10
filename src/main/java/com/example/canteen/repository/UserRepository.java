package com.example.canteen.repository;

import com.example.canteen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
//
//
//    default User getUser(UserPrincipal currentUser) {
//        return getUserByName(currentUser.getUsername());
//    }
//
//    default User getUserByName(String username) {
//        return findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
//    }
}
