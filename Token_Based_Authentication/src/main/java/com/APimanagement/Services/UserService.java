package com.APimanagement.Services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.APimanagement.Entities.Role;
import com.APimanagement.Entities.Users;
import com.APimanagement.Repository.RoleRepository;
import com.APimanagement.Repository.UserRepository;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

   
    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a user with a role fetched by role name
    public void register(String username, String password, Long roleId) {
        // Fetch the role by its name
        Role role = roleRepository.findByRoleId(roleId);
        if (role == null) {
            throw new RuntimeException("Role not found: " + roleId);
        }

        // Create the new user
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Encode the password
        user.setRole(role); // Set the role

        // Save the user
        userRepository.save(user);
    }


    // Get user by username (for login)
    public Users findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Invalid Username"));
    }

    // Get all users with pagination (admin only, based on role)
    public Page<Users> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    //get the user by id
    public Users getUserById(Long id) {
        return userRepository.findById(id).orElseGet(null);
    }
    
    // Get the role of a user (for token validation)
    public String getRoleByUsername(String username) {
        Users user = findByUsername(username);
        return user.getRole().getRoleName(); // Assuming Role is a class with 'roleName' field
    }
}
