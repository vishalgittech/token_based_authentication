package com.APimanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APimanagement.Entities.Department;

@Repository
public interface DeptRepo extends JpaRepository<Department, Long> {

}
