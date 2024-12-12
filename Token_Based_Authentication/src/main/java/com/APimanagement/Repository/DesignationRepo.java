package com.APimanagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APimanagement.Entities.Designation;

@Repository
public interface DesignationRepo extends JpaRepository<Designation, Long> {

}
