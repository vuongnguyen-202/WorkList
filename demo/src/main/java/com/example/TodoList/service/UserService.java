package com.example.TodoList.service;

import com.example.TodoList.dto.UserDto;
import com.example.TodoList.model.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);

    List<UserDto> findAllUsers();
}