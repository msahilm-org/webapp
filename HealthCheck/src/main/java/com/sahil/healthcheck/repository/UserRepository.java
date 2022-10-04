package com.sahil.healthcheck.repository;

import com.sahil.healthcheck.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findUserByUsername(String username);
}
