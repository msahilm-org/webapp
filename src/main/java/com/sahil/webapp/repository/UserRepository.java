package com.sahil.webapp.repository;

import com.sahil.webapp.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    User findUserByUsername(String username);
}
