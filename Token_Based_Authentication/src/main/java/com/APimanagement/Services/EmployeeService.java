package com.APimanagement.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.APimanagement.Dto.EmployeeDTO;
import com.APimanagement.Entities.Department;
import com.APimanagement.Entities.Designation;
import com.APimanagement.Entities.Employee;
import com.APimanagement.Entities.Role;
import com.APimanagement.Entities.Users;
import com.APimanagement.Repository.DeptRepo;
import com.APimanagement.Repository.DesignationRepo;
import com.APimanagement.Repository.EmployeeRepository;
import com.APimanagement.Repository.RoleRepository;
import com.APimanagement.Repository.UserRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private  DesignationRepo designationRep;
	
	@Autowired
	private DeptRepo departmentRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	public Employee createEmployee(EmployeeDTO employeeDto) {
	    // Get the username of the authenticated user from SecurityContextHolder
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String createdByUsername = authentication.getName(); // Get the username of the authenticated user
	    
	    // Retrieve the user who is creating the employee
	    Users createdBy = userRepository.findByUsername(createdByUsername)
	            .orElseThrow(() -> new RuntimeException("User not found"));

	    // Retrieve the related entities (Department, Designation, Role) based on IDs in DTO
	    Department department = departmentRepo.findById(employeeDto.getDeptId())
	            .orElseThrow(() -> new RuntimeException("Department not found"));

	    Designation designation = designationRep.findById(employeeDto.getDesignationId())
	            .orElseThrow(() -> new RuntimeException("Designation not found"));

	    Role role = roleRepository.findById(employeeDto.getRoleId())
	            .orElseThrow(() -> new RuntimeException("Role not found"));

	    // Create Employee entity and populate it from DTO
	    Employee employee = new Employee();
	    employee.setFirstName(employeeDto.getFirstName());
	    employee.setLastName(employeeDto.getLastName());
	    employee.setEmail(employeeDto.getEmail());
	    employee.setDepartment(department);
	    employee.setDesignation(designation);
	    employee.setRole(role);
	    employee.setCreatedBy(createdBy); // Set the user who created the employee

	    // Save and return the employee entity
	    return employeeRepository.save(employee);
	}

    // Get employee by ID
    public Employee getEmployeeById(Long empId) {
        return employeeRepository.findById(empId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // Get all employees (admin only)
    public Page<Employee> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public Employee updateEmployee(Long id, EmployeeDTO employeeDTO) {
        // Get the username from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // Extracted username from JWT

        // Fetch the user who is updating the employee
        Users createdBy = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Fetch the existing employee
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Fetch department and designation based on DTO (no role updates here)
        Department department = departmentRepo.findById(employeeDTO.getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Designation designation = designationRep.findById(employeeDTO.getDesignationId())
                .orElseThrow(() -> new RuntimeException("Designation not found"));

        // Update fields of the existing employee with the new data from DTO
        existingEmployee.setFirstName(employeeDTO.getFirstName());
        existingEmployee.setLastName(employeeDTO.getLastName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setDepartment(department);
        existingEmployee.setDesignation(designation);
        // Do not update role or createdBy

        // Save the updated employee and return it
        return employeeRepository.save(existingEmployee);
    }

    
    
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public boolean isCurrentUser(Long employeeId, String username) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return employee.getCreatedBy().getUsername().equals(username); // Compare the logged-in user's username
    }
}
    
    
    


