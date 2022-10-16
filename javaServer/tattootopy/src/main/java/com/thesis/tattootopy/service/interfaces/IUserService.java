package com.thesis.tattootopy.service.interfaces;

import com.thesis.tattootopy.model.Tattoo;
import com.thesis.tattootopy.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {
    List<User> getAllUsers();
    List<Tattoo> findAllTattoosOfUser(String email);
    User findUserByEmail(String email);
    boolean save(User user);
    boolean update(User user);
    boolean deleteUserByEmail(String email);

}