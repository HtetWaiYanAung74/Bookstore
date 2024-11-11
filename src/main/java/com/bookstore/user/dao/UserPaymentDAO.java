package com.bookstore.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.user.domain.UserPayment;

public interface UserPaymentDAO extends CrudRepository<UserPayment, Long> {

	
	
}
