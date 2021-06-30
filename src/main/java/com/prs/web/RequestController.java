package com.prs.web;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.Request;
import com.prs.business.User;
import com.prs.db.RequestRepo;
import com.prs.db.UserRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")

public class RequestController {
	
	@Autowired
	private RequestRepo requestRepo;
	@Autowired
	private UserRepo userRepo;
	
	@GetMapping("/")
	public Iterable<Request> getAll() {
		return requestRepo.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<Request> get(@PathVariable int id) {
		return requestRepo.findById(id);
	}
	
	@PostMapping("/")
	public Request add(@RequestBody Request request) {
		request.setStatus("New");
		LocalDateTime currentDateTime = LocalDateTime.now();
		request.setSubmittedDate(currentDateTime.toLocalDate());
		return requestRepo.save(request);
	}
	
	@PutMapping("/")
	public Request update(@RequestBody Request request) {
		return requestRepo.save(request);
	}
	
	@DeleteMapping("/{id}")
	public Optional<Request> delete(@PathVariable int id) {
		Optional<Request> request = requestRepo.findById(id);
		if (request.isPresent()) {
			try {
		requestRepo.deleteById(id);
			}
			catch (DataIntegrityViolationException dive) {
				//catch dive when request exists as fk on another table
				System.err.println(dive.getRootCause().getMessage());
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Foreign Key Constraint Issue - Request id: "+id+" "
								+ "is referred to elsewhere");
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Exception caught during request delete.");
			}
	}
		else {
			System.err.println("Request delete error - no request found for id:"+id);
		}
		return request;
}
	
	@PutMapping("/submit-review")
	public Request updateStatus(@RequestBody Request request) {
		if (request.getTotal() <= 50.0) {
			request.setStatus("Approved");
		}else {
			request.setStatus("Review");
		}
		return requestRepo.save(request);
	}
	
//	@PutMapping("/list-review/{id}")
//	public Iterable<Request> getRequestbyStatusAndUserId(@PathVariable String status, int id) {
//		if (status == "review" && User.user != id) {
//		
//		}
//	}
}

