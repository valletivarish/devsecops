package com.devsecops.myapp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsecops.myapp.model.Customer;
import com.devsecops.myapp.service.CustomerService;
import com.devsecops.myapp.serviceimpl.CustomerServiceImpl;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
	
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody Customer c) {
        logger.info("Creating new customer...{}",c);
        logger.info("Creating new customer...{}",c);
        logger.info("Creating new customer...{}",c);
        Customer created = service.create(c);
        logger.info("Customer created with ID: {}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> get(@PathVariable Long id) {
        logger.info("Fetching customer with ID: {}", id);
        return service.get(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    logger.warn("Customer not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping
    public List<Customer> list() {
        logger.info("Listing all customers");
        return service.list();
    }

    @PutMapping("/{id}")
    public Customer update(@PathVariable Long id, @RequestBody Customer c) {
        logger.info("Updating customer with ID: {}", id);
        return service.update(id, c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        logger.info("Deleting customer with ID: {}", id);
        service.delete(id);
    }

    @GetMapping("/ping")
    public String ping(@RequestParam String host) {
        logger.info("Pinging host: {}", host);
        return ((CustomerServiceImpl) service).runPing(host);
    }

    @GetMapping("/md5")
    public String md5(@RequestParam String s) {
        logger.info("Generating MD5 hash for input");
        return service.hashMd5(s);
    }
}
