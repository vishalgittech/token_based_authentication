package com.APimanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APimanagement.Entities.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository <Employee, Long>{

	
}
