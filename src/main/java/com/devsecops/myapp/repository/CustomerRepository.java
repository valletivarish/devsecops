package com.devsecops.myapp.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.devsecops.myapp.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	@Query(value = "SELECT * FROM customers WHERE email = '" + ":email'", nativeQuery = true)
	Optional<Customer> findUnsafeByEmail(@Param("email") String email);

	Optional<Customer> findByEmail(String email);
}