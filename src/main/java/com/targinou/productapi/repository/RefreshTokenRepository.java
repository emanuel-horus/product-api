package com.targinou.productapi.repository;

import com.targinou.productapi.model.RefreshToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends GenericRepository<RefreshToken>{

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    @Query("UPDATE RefreshToken SET isUsed = true WHERE user.id = :userId")
    void updateIsUsedByUserId(@Param("userId") Long userId);

}
