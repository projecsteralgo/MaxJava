package com.maxtrain.prs.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestsController {
	@Autowired
	private RequestRepository requestRepo;
	@GetMapping
	public ResponseEntity<Iterable<Request>> GetAll() {
		var requests = requestRepo.findAll();
		return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.OK);
	}
	@GetMapping("{id}")
	public ResponseEntity<Request> GetById(@PathVariable int id) {
		var request = requestRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Request>(request.get(), HttpStatus.OK);
	}
	@PostMapping
	public ResponseEntity<Request> Insert(@RequestBody Request request) {
		if(request == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		request.setId(0);
		var newRequest = requestRepo.save(request);
		return new ResponseEntity<Request>(newRequest, HttpStatus.CREATED);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity Update(@PathVariable int id, @RequestBody Request request) {
		if(request.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var oldRequest = requestRepo.findById(request.getId());
		if(oldRequest.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		requestRepo.save(request);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity Delete(@PathVariable int id) {
		var request = requestRepo.findById(id);
		if(request.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		requestRepo.deleteById(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	@GetMapping("user/{userId}")
	public ResponseEntity<Iterable<Request>> GetOrderNotCustomer(@PathVariable int userId) {
		var requests = requestRepo.findByUserIdNot(userId);
		return new ResponseEntity<Iterable<Request>>(requests, HttpStatus.I_AM_A_TEAPOT);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("review/{id}")
	public ResponseEntity SetOrderToZero(@PathVariable int id, @RequestBody Request request) {
		var newStatus = request.getTotal() <= 100 ? "Approve" : "Review";
		request.setStatus(newStatus);
		return Update(id, request);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("approve/{id}")
	public ResponseEntity SetOrderTo5000(@PathVariable int id, @RequestBody Request request) {
		request.setStatus("Approve");
		return Update(id, request);
	}
	@SuppressWarnings("rawtypes")
	@PutMapping("reject/{id}")
	public ResponseEntity SetOrderToNegative(@PathVariable int id, @RequestBody Request request) {
		request.setStatus("Rejected");
		return Update(id,request);
	}
}
