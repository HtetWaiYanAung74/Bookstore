package com.bookstore.user.service;

import com.bookstore.user.domain.Payment;
import com.bookstore.user.domain.UserPayment;

public interface PaymentService {

	Payment setByUserPayment(UserPayment userPayment, Payment payment);
	
}
