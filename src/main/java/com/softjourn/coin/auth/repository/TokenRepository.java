package com.softjourn.coin.auth.repository;

import com.softjourn.coin.auth.entity.Token;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, Long> {

    @Query("SELECT t.tokenValue FROM Token t WHERE t.expirationTime > CURRENT_TIMESTAMP")
    List<String> getAliveTokens();

    @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
            "FROM Token t " +
            "WHERE t.expirationTime > CURRENT_TIMESTAMP " +
            "AND t.tokenValue = ?1")
    boolean isTokenAlive(String tokenValue);

    @Modifying
    @Query("DELETE FROM Token t WHERE t.expirationTime < CURRENT_TIMESTAMP")
    void deleteExpired();

    @Modifying
    @Query("DELETE FROM Token t WHERE t.tokenValue = ?1")
    void deleteByValue(String value);
}
