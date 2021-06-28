package com.prs.web;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.LineItem;
import com.prs.db.LineItemRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/lineitems")

public class LineItemController {
	
	@Autowired
	private LineItemRepo lineItemRepo;
	
	@GetMapping("/")
	public Iterable<LineItem> getAll() {
		return lineItemRepo.findAll();
	}
	
	@GetMapping("/{id}")
	public Optional<LineItem> get(@PathVariable int id) {
		return lineItemRepo.findById(id);
	}
	
	@PostMapping("/")
	public LineItem add(@RequestBody LineItem lineItem) {
		return lineItemRepo.save(lineItem);
	}
	
	@PutMapping("/")
	public LineItem update(@RequestBody LineItem lineItem) {
		return lineItemRepo.save(lineItem);
	}
	
	@DeleteMapping("/{id}")
	public Optional<LineItem> delete(@PathVariable int id) {
		Optional<LineItem> lineItem = lineItemRepo.findById(id);
		if (lineItem.isPresent()) {
			try {
		lineItemRepo.deleteById(id);
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Exception caught during LineItem delete.");
			}
	}
		else {
			System.err.println("LineItem delete error - no lineItem found for id:"+id);
		}
		return lineItem;
}
}

