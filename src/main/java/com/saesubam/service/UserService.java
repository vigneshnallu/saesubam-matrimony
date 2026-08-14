package com.saesubam.service;

import java.util.List;
import com.saesubam.model.MembershipType;
import com.saesubam.model.Users;

public interface UserService {

    Users findByEmail(String email);

    List<Users> getAllUsers();

    Users getUserById(Long id);

    Users createUser(Users user);

    Users updateUser(Users user);

    void deleteUser(Long id);

    Users authenticate(String email, String password);

    Users upgradeMembership(Long userId, MembershipType membershipType);
}