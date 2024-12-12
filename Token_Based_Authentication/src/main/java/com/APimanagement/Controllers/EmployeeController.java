package com.APimanagement.Controllers;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.APimanagement.Dto.EmployeeDTO;
import com.APimanagement.Entities.*;
import com.APimanagement.Services.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    // Create a new employee (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            
            // Parse JSON and map to EmployeeDTO (assuming your EmployeeDTO has a constructor or setter methods)
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setFirstName(jsonObject.getString("firstName"));
            employeeDTO.setLastName(jsonObject.getString("lastName"));
            employeeDTO.setEmail(jsonObject.getString("email"));
            employeeDTO.setDeptId(jsonObject.getLong("deptId"));
            employeeDTO.setDesignationId(jsonObject.getLong("designationId"));
            employeeDTO.setRoleId(jsonObject.getLong("roleId"));

            Employee createdEmployee = employeeService.createEmployee(employeeDTO);
            return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @employeeService.isCurrentUser(#id, authentication.name)")
    public ResponseEntity<Employee> updateEmployee(@RequestBody @Valid String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            Long id = jsonObject.getLong("empId");

            // Map the JSON data to EmployeeDTO for updating
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setFirstName(jsonObject.getString("firstName"));
            employeeDTO.setLastName(jsonObject.getString("lastName"));
            employeeDTO.setEmail(jsonObject.getString("email"));
            employeeDTO.setDeptId(jsonObject.getLong("deptId"));
            employeeDTO.setDesignationId(jsonObject.getLong("designationId"));

            // Update employee details
            Employee updatedEmployee = employeeService.updateEmployee(id, employeeDTO);
            return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    
    // Delete employee (Admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteEmployee(@RequestBody @Valid String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            
            // Extract ID from JSON
            Long id = jsonObject.getLong("id");
            
            employeeService.deleteEmployee(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Endpoint for fetching employee details, accessible by ADMIN and the employee themselves
    @PreAuthorize("hasRole('ADMIN') or @employeeService.isCurrentUser(#id, authentication.name)") // Allow ADMIN or the user themselves
    @GetMapping("/viewById")
    public ResponseEntity<Employee> getEmployeeById(@RequestBody @Valid String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);

            // Extract ID from JSON
            Long id = jsonObject.getLong("id");
            Employee employee = employeeService.getEmployeeById(id);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint for fetching all employees with pagination
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/view")
    public ResponseEntity<Page<Employee>> getAllUsers(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(pageNum, size);
        Page<Employee> employeesPage = employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(employeesPage);
    }
}
