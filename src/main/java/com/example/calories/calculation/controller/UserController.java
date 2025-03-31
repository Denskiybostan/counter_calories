package com.example.calories.calculation.controller;

import com.example.calories.calculation.model.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.calories.calculation.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(@RequestParam(required = false) String goal,
                               @RequestParam(required = false) String email) {
        return userService.getUsers(goal, email);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable @NotNull Long userId) {
        return userService.getUserById(userId)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK)) // Если пользователь найден
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND)); // Если пользователя нет
    }

    @GetMapping("/{userId}/calories")
    public ResponseEntity<Double> getDailyCalories(@PathVariable @NotNull Long userId) {
        return userService.getUserById(userId)
                .map(user -> new ResponseEntity<>(userService.calculateDailyCalories(user), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
