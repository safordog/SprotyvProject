package com.gmail.safordog.newsreport.securityservices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gmail.safordog.newsreport.repository.security.UserRepository;
import com.gmail.safordog.newsreport.model.user.User;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User find(String userName) {
        return userRepository.findUserByUsername(userName);
    }

    public User find(Long id) {
        return userRepository.findUserById(id);
    }
}
