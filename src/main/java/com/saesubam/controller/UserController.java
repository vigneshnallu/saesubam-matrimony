/*
 * 
 */
package com.saesubam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saesubam.model.Users;
import com.saesubam.service.UserService;

/**
 * The Class UserController.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    /** The user service. */
    @Autowired
    private UserService userService;

    /**
     * Gets the all users.
     *
     * @return the all users
     */
    @GetMapping
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Gets the user by id.
     *
     * @param id the id
     * @return the user by id
     */
    @GetMapping("/{id}")
    public Users getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    /**
     * Creates the user.
     *
     * @param user the user
     * @return the user
     */
    @PostMapping
    public Users createUser(@RequestBody Users user) {
        return userService.createUser(user);
    }

    /**
     * Update user.
     *
     * @param id the id
     * @param user the user
     * @return the user
     */
    @PutMapping("/{id}")
    public Users updateUser(@PathVariable Long id, @RequestBody Users user) {
        user.setId(id);
        return userService.updateUser(user);
    }

    /**
     * Delete user.
     *
     * @param id the id
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
