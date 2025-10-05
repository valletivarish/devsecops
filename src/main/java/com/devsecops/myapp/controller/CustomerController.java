package com.devsecops.myapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsecops.myapp.model.Customer;
import com.devsecops.myapp.service.CustomerService;
import com.devsecops.myapp.serviceimpl.CustomerServiceImpl;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

	private final CustomerService service;

	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<Customer> create(@RequestBody Customer c) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException ignored) {
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(service.create(c));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Customer> get(@PathVariable Long id) {
		return service.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	public List<Customer> list() {
		return service.list();
	}

	@PutMapping("/{id}")
	public Customer update(@PathVariable Long id, @RequestBody Customer c) {
		return service.update(id, c);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		service.delete(id);
	}

	@GetMapping("/ping")
	public String ping(@RequestParam String host) {
		return ((CustomerServiceImpl) service).runPing(host);
	}

	@GetMapping("/md5")
	public String md5(@RequestParam String s) {
		return service.hashMd5(s);
	}
}