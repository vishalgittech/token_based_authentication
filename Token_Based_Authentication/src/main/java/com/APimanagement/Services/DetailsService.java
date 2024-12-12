package com.APimanagement.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.APimanagement.Entities.Department;
import com.APimanagement.Entities.Designation;
import com.APimanagement.Entities.Role;
import com.APimanagement.Repository.DeptRepo;
import com.APimanagement.Repository.DesignationRepo;
import com.APimanagement.Repository.RoleRepository;

@Service

public class DetailsService {
		@Autowired
		private DeptRepo departmentRepository;
		
		@Autowired
        private DesignationRepo designationRepository;
		
		 @Autowired
		    private RoleRepository roleRepository;

	    // Method to get Department by ID
	    public Department findDeptById(Long deptId) {
	        return departmentRepository.findById(deptId).orElseThrow(() -> new RuntimeException("Department not found"));
	    }
	    
	    public Designation findDesignationById(Long designationId) {
	        return designationRepository.findById(designationId).orElseThrow(() -> new RuntimeException("Designation not found"));
	    }
	   

	    // Method to get Role by ID
	    public Role findRoleById(Long roleId) {
	        return roleRepository.findById(roleId).orElseThrow(() -> new RuntimeException("Role not found"));
	    }
}


