package com.APimanagement.Dto;

import lombok.Data;

@Data

public class EmployeeDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Long deptId;
    private Long designationId;
    private Long roleId;

    // Getters and Setters
}
