package com.gmail.safordog.newsreport.controller;

import com.gmail.safordog.newsreport.model.user.User;
import com.gmail.safordog.newsreport.securityservices.UserService;
import com.gmail.safordog.newsreport.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("account")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {

    public static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private UserService userService;

    @CrossOrigin
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User newUser) {
        if (userService.find(newUser.getUsername()) != null) {
            logger.error("username Already exist " + newUser.getUsername());
            return new ResponseEntity(
                    new CustomErrorType("user with username " + newUser.getUsername() + "already exist "),
                    HttpStatus.CONFLICT);
        }
        newUser.setRole("USER");

        return new ResponseEntity<User>(userService.save(newUser), HttpStatus.CREATED);
    }

    @CrossOrigin
    @RequestMapping(value = "/login")
    public Principal user(Principal principal) {
        logger.info("user logged " + principal);
        return principal;
    }

}
