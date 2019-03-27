package com.gmail.safordog.newsreport.repository.security;

import com.gmail.safordog.newsreport.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findOneByUsername(String username);
    User findUserByUsername(String username);
    User findUserById(Long id);
}
