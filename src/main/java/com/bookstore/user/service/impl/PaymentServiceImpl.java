package com.bookstore.user.service.impl;

import org.springframework.stereotype.Service;

import com.bookstore.user.domain.Payment;
import com.bookstore.user.domain.UserPayment;
import com.bookstore.user.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Override
	public Payment setByUserPayment(UserPayment userPayment, Payment payment) {
		// TODO Auto-generated method stub
		
		payment.setCardName(userPayment.getCardName());
		payment.setCardNumber(userPayment.getCardNumber());
		payment.setCvc(userPayment.getCvc());
		payment.setExpiryMonth(userPayment.getExpiryMonth());
		payment.setExpiryYear(userPayment.getExpiryYear());
		payment.setHolderName(userPayment.getHolderName());
		payment.setType(userPayment.getType());
		
		return payment;
	}
	
}
