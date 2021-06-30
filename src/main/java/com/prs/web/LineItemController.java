package com.prs.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.business.LineItem;
import com.prs.business.Request;
import com.prs.db.LineItemRepo;
import com.prs.db.RequestRepo;

@CrossOrigin
@RestController
@RequestMapping("/api/line-items")

public class LineItemController {

	@Autowired
	private LineItemRepo lineItemRepo;
	@Autowired
	private RequestRepo requestRepo;

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
		LineItem li = lineItemRepo.save(lineItem);
		if (recalculateTotalValue(lineItem.getRequest())) {
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Exception caught during movieCollection post.");
		}
		return li;
	}

	@PutMapping("/")
	public LineItem update(@RequestBody LineItem lineItem) {
		LineItem li = lineItemRepo.save(lineItem);
		if (recalculateTotalValue(lineItem.getRequest())) {
		} else {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Exception caught during movieCollection post.");
		}
		return li;
	}

	@DeleteMapping("/{id}")
	public Optional<LineItem> delete(@PathVariable int id) {
		Optional<LineItem> lineItem = lineItemRepo.findById(id);
		if (lineItem.isPresent()) {
			try {
				lineItemRepo.deleteById(id);
				if (!recalculateTotalValue(lineItem.get().getRequest())) {
					throw new Exception("Issue recalculating totalValue on delete.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"Exception caught during LineItem delete.");
			}
		} else {
			System.err.println("LineItem delete error - no lineItem found for id:" + id);
		}
		return lineItem;
	}

	@GetMapping("/lines-for-pr/{id}")
	public Iterable<LineItem> getAllByRequest(@PathVariable int id) {
		return lineItemRepo.findAllByRequestId(id);
	}
	
	private boolean recalculateTotalValue(Request request) {
		boolean success = false;
		List<LineItem> lis = lineItemRepo.findAllByRequestId(request.getId());
		double total = 0.0;
		for (LineItem li: lis) {
			total += (li.getProduct().getPrice()*li.getQuantity());
		}
		request.setTotal(total);
		requestRepo.save(request);
		success = true;
		
		return success;
	}

}
