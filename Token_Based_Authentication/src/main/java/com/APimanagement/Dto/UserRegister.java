package com.APimanagement.Dto;

import lombok.Data;

@Data
public class UserRegister {
    private String username;
    private String password;
    private Long roleId;


}
