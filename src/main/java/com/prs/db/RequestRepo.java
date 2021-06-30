package com.prs.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.prs.business.Request;

public interface RequestRepo extends CrudRepository<Request, Integer> {
	
	public List<Request> getRequestbyStatusAndUserId(String status, int id);
	
}