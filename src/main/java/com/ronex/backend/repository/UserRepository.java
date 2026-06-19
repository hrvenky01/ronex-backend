package com.ronex.backend.repository;

import com.ronex.backend.model.User;
import com.ronex.backend.dto.UserGrowthDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPhone(String phone);

    @Query("""
        SELECT new com.ronex.backend.dto.UserGrowthDto(
            DATE(u.createdAt),
            COUNT(u)
        )
        FROM User u
        GROUP BY DATE(u.createdAt)
        ORDER BY DATE(u.createdAt)
    """)
    List<UserGrowthDto> getUserGrowth();
}