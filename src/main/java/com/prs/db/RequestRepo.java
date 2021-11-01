package com.prs.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.prs.business.Request;

public interface RequestRepo extends CrudRepository<Request, Integer> {
	
	List<Request> findAllByStatusAndUserIdNot(String status, int id);
	
}