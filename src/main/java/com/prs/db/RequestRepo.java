package com.prs.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.prs.business.Request;
import com.prs.business.User;

public interface RequestRepo extends CrudRepository<Request, Integer> {
	
	List<Request> findAllByStatusAndUserIdNot(String status, int id);
	
}