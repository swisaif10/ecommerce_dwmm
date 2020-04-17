package com.vaibhav.sec.repo;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vaibhav.sec.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);
  
	List<User> findByIdIn(List<Long> userIds);
 
 
 
	Boolean existsByEmail(String email);
}
