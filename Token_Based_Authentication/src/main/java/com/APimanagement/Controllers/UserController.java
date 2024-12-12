package com.APimanagement.Controllers;


import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.APimanagement.Entities.Users;

import com.APimanagement.Services.UserService;


	@RestController
	@RequestMapping("/api/auth")
	public class UserController {

	    private final UserService userService;

	    public UserController(UserService userService) {
	        this.userService = userService;
	    }
	    @GetMapping
	    public ResponseEntity<Page<Users>> getAllUsers(
	            @RequestParam(defaultValue = "0") int pageNum,
	            @RequestParam(defaultValue = "10") int size
	    ) {
	        Pageable pageable = PageRequest.of(pageNum, size);
	        Page<Users> usersPage = userService.getAllUsers(pageable);
	        return ResponseEntity.ok(usersPage);
	    }
 
}