package com.bookstore.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.user.dao.UserPaymentDAO;
import com.bookstore.user.domain.UserPayment;
import com.bookstore.user.service.UserPaymentService;

@Service
public class UserPaymentServiceImpl implements UserPaymentService {

	@Autowired
	private UserPaymentDAO userPaymentDAO;
	
	@Override
	public UserPayment findById(Long id) {
		// TODO Auto-generated method stub
		return userPaymentDAO.findById(id).get();
		
	}

	@Override
	public void removeById(Long id) {
		// TODO Auto-generated method stub		
		userPaymentDAO.deleteById(id);
		
	}

}
