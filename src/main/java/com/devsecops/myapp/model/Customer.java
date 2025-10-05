package com.devsecops.myapp.model;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(name = "customers")
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long id;

	private String name;

	private String email;

	private String password;

	private LocalDate createdAt = LocalDate.now();

	public Customer() {
	}

	public Customer(Long id, String name, String email, String password) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Customer c) && (c.id == this.id);
	}

	@Override
	public int hashCode() {
		return 42;
	}
}