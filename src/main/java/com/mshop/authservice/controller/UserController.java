package com.mshop.authservice.controller;

import com.mshop.authservice.client.CartClient;
import com.mshop.authservice.client.FileClient;
import com.mshop.authservice.dto.Cart;
import com.mshop.authservice.entity.User;
import com.mshop.authservice.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/users")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    CartClient cartClient;

    @Autowired
    FileClient fileClient;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.findByStatusTrueAndRoleFalse();
        setUsersImage(users);
        return ResponseEntity.ok(users);
    }

    @GetMapping("{id}")
    public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(setUserImageBase64(userService.findById(id).get()));
    }

    @GetMapping("email/{email}")
    public ResponseEntity<User> getOneByEmail(@PathVariable("email") String email) {
        return ResponseEntity.ok(setUserImageBase64(userService.findByEmail(email)));
    }

    @PostMapping
    public ResponseEntity<User> post(@RequestBody User user) {
        if (userService.existsByEmail(user.getEmail())) {
            return ResponseEntity.notFound().build();
        }
        if (userService.existsById(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        String encodedPw = DigestUtils
                .md5Hex(user.getPassword())
                .toUpperCase();
        user.setPassword(encodedPw);
        // luu file vao minio. xong luu key vao db. luc nao lay user ra thi call file service lay image qua key
        String imageKey = fileClient.saveFileString(user.getImage());
        user.setImage(imageKey);
        User u = userService.save(user);
        Cart c = new Cart(0L, 0.0, user.getAddress(), user.getPhone(), true, u.getUserId());
        cartClient.addCartUser(u.getUserId(), c);
        return ResponseEntity.ok(u);
    }

    @PutMapping("{id}")
    public ResponseEntity<User> put(@PathVariable("id") Long id, @RequestBody User user) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (!id.equals(user.getUserId())) {
            return ResponseEntity.badRequest().build();
        }
        String image = user.getImage();
        if (image != null && image.startsWith("data:image/jpeg;base64")) {
            String imageKey = fileClient.saveFileString(image);
            user.setImage(imageKey);
        }
        return ResponseEntity.ok(userService.save(user));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!userService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
//		Crepo.deleteByUserId(id);
        User u = userService.findById(id).get();
        u.setStatus(false);
        userService.save(u);
//		repo.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/exist-by-user-id")
    public Boolean existByUserId(@RequestParam("userId") Long userId) {
        return userService.existsById(userId);
    }

    @GetMapping("/exist-by-email")
    public Boolean existByEmail(@RequestParam("email") String email) {
        return userService.existsByEmail(email);
    }

    @PostMapping("/get-all-by-ids")
    public ResponseEntity<List<User>> getAllByIds(@RequestBody List<Long> ids) {
        List<User> users = userService.findAllByIds(ids);
        return ResponseEntity.ok(setUsersImage(users));
    }

    private List<User> setUsersImage(List<User> users) {
        if (ObjectUtils.isEmpty(users)) {
            return List.of();
        }
        Map<String, User> userAndImage = new HashMap<>();
        for (User user : users) {
            if (user.getImage() != null) {
                userAndImage.put(user.getImage(), user);
            }
        }
        if (!ObjectUtils.isEmpty(userAndImage)) {
            Map<String, String> imageMap = fileClient.getFileStrings(userAndImage.keySet());
            if (!ObjectUtils.isEmpty(imageMap)) {
                for (var entry : userAndImage.entrySet()) {
                    String imageBase64 = imageMap.get(entry.getKey());
                    entry.getValue().setImage(imageBase64);
                }
            }
        }
        return users;
    }


    private User setUserImageBase64(User user) {
        if (user.getImage() != null) {
            user.setImage(fileClient.getFileString(user.getImage()));
        }
        return user;
    }

}
