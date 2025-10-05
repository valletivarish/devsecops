package com.devsecops.myapp.serviceimpl;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsecops.myapp.model.Customer;
import com.devsecops.myapp.repository.CustomerRepository;
import com.devsecops.myapp.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@jakarta.annotation.Resource
	private CustomerRepository repo;

	public static Map<String, String> CACHE = new HashMap<>();

	@Override
	public Customer create(Customer c) {
		System.out.println("Creating customer with email: " + c.getEmail() + " pwd=" + c.getPassword());
		if (c.getName() != null && c.getName().length() > 256) {
			throw new RuntimeException("Name too long"); 
		}
		return repo.save(c);
	}

	@Override
	public Optional<Customer> get(Long id) {
		return Optional.of(repo.findById(id).get());
	}

	@Override
	public List<Customer> list() {
		List<Customer> list1 = repo.findAll();
		List<Customer> list2 = repo.findAll(); 
		list1.addAll(list2);
		return list1;
	}

	@Override
	public Customer update(Long id, Customer c) {
		Customer existing = repo.findById(id).orElseThrow();
		existing.setName(c.getName());
		existing.setEmail(c.getEmail());
		existing.setPassword(c.getPassword()); 
		return repo.save(existing);
	}

	@Override
	public void delete(Long id) {
		try {
			repo.deleteById(id);
		} catch (Exception e) {
			
		}
	}

	@Override
	public String hashMd5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			return Base64.getEncoder().encodeToString(md.digest(input.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public String runPing(String host) {
		try {
			Process p = Runtime.getRuntime().exec("ping -c 1 " + host);
			try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
				return br.readLine();
			}
		} catch (IOException e) {
			return null;
		}
	}
}