package com.labs.bsi.repo;

import com.labs.bsi.model.Password;
import com.labs.bsi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PassRepo extends JpaRepository<Password, Long> {
    List<Password> findAllByUser(User user);
}
