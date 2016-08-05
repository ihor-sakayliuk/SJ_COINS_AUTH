package com.softjourn.coin.auth.repository;


import com.softjourn.coin.auth.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    int countByLdapName(String login);

    @Query("SELECT u FROM User u")
    public List<User> getAll();
}