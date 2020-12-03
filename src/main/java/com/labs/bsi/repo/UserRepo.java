package com.labs.bsi.repo;

import com.labs.bsi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByLogin(String login);
}
