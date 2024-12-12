package com.APimanagement.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.APimanagement.Entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	 Role findByRoleId(Long roleId);
	
}
