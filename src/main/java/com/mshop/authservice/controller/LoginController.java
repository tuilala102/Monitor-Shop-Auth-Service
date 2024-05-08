package com.mshop.authservice.controller;

import com.mshop.authservice.client.FileClient;
import com.mshop.authservice.dto.Login;
import com.mshop.authservice.entity.User;
import com.mshop.authservice.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

	@Autowired
    UserService userService;

    @Autowired
    FileClient fileClient;

    @RequestMapping("/admin")
    public ResponseEntity<User> LoginAdmin(@RequestBody Login login) {
        User u = new User();
        boolean checkUsername = false;
        List<User> listU = userService.findAllAdmin();
        for (User user : listU) {
            if (login.getUsername().equals(user.getEmail())) {
                checkUsername = true;
                u = userService.findByEmail(login.getUsername());
                String encodedPw = DigestUtils
                        .md5Hex(login.getPassword())
                        .toUpperCase();
                if (u.getPassword().equals(encodedPw)) {
                    u.setImage(fileClient.getFileString(u.getImage()));
                    return ResponseEntity.ok(u);
                }
            }
        }
        if (!checkUsername) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @RequestMapping("/user")
    public ResponseEntity<User> LoginUser(@RequestBody Login login) {
        User u = new User();
        boolean checkUsername = false;
        List<User> listU = userService.findAllUser();
        System.out.println(listU.size());
        for (User user : listU) {
            if (login.getUsername().equals(user.getEmail())) {
                checkUsername = true;
                u = userService.findByEmail(login.getUsername());
                String encodedPw = DigestUtils
                        .md5Hex(login.getPassword())
                        .toUpperCase();
                if (u.getPassword().equalsIgnoreCase(encodedPw)) {
                    u.setImage(fileClient.getFileString(u.getImage()));
                    return ResponseEntity.ok(u);
                }
            }
        }
        if (!checkUsername) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
