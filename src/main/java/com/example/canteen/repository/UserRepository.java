package com.example.canteen.repository;

import com.example.canteen.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

//    Boolean existsByUsername(@NotBlank String username);
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
