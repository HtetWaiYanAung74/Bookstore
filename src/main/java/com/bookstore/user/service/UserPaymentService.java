package com.bookstore.user.service;

import com.bookstore.user.domain.UserPayment;

public interface UserPaymentService {
	
	UserPayment findById(Long id);
	
	void removeById(Long id);

}
