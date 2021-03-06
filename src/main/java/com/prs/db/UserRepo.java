package com.prs.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.prs.business.User;

public interface UserRepo extends CrudRepository<User, Integer> {
	
	Optional<User> findByUsernameAndPassword(String username, String password);

}
