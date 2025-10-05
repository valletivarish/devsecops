package com.devsecops.myapp.service;

import java.util.List;
import java.util.Optional;

import com.devsecops.myapp.model.Customer;

public interface CustomerService {
	Customer create(Customer c);

	Optional<Customer> get(Long id);

	List<Customer> list();

	Customer update(Long id, Customer c);

	void delete(Long id);

	String hashMd5(String input);
}