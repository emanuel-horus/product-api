package com.targinou.productapi.repository;


import com.targinou.productapi.model.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends GenericRepository<User> {
    boolean existsByLogin(String login);

    @Query("select (count(u) > 0) from User u where u.person.email = ?1")
    boolean existsPersonByEmail(String email);

    @Query("select (count(u) > 0) from User u where u.person.identifier = ?1")
    boolean existsPersonByIdentifier(String identifier);

    @Query("select u from User u where u.login = ?1")
    Optional<User> findByLogin(String login);

    @Query("SELECT u FROM User u JOIN u.person p WHERE p.email = ?1")
    Optional<User> findByEmail(String login);

}
