package com.gmail.safordog.newsreport.securityservices;

import com.gmail.safordog.newsreport.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BCryptPasswordEncoder encoder = passwordEncoder();
        User user = userService.find(username);
        if(user == null){
            throw new UsernameNotFoundException("User Name "+username +"Not Found");
        }
        return new org.springframework.security.core.userdetails.User
                (user.getFullName(), encoder.encode(user.getPassword()), user.getAuthorities());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
