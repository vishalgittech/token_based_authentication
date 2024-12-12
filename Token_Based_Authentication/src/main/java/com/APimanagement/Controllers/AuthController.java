package com.APimanagement.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.APimanagement.Dto.LoginRequest;
import com.APimanagement.Dto.UserRegister;
import com.APimanagement.Entities.Role;
import com.APimanagement.Services.JwtService;
import com.APimanagement.Services.UserService;
import com.APimanagement.Repository.RoleRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Constructor Injection (Better practice than field injection)
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, RoleRepository roleRepository) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegister request) {
        String username = request.getUsername();
        String password = request.getPassword();
        Long roleId = request.getRoleId();

        // Fetch role from RoleRepository
        Role role = roleRepository.findByRoleId(roleId);

        try {
            // Register the user
            userService.register(username, password, roleId);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        // Fetch the user's role from the userService (assuming it returns the role based on the username)
        String role = userService.getRoleByUsername(loginRequest.getUsername());

        // Generate a JWT token for the user
        String token = jwtService.generateToken(loginRequest.getUsername(), role);

        // Return the token in the response
        return ResponseEntity.ok(token	); // Response wrapped in JwtResponse DTO
    }

    // DTO for JWT response
    public static class JwtResponse {
        private String token;

        public JwtResponse(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
