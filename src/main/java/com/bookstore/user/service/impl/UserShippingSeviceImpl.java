package com.bookstore.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.user.dao.UserShippingDAO;
import com.bookstore.user.domain.UserShipping;
import com.bookstore.user.service.UserShippingService;

@Service
public class UserShippingSeviceImpl implements UserShippingService {

	@Autowired
	private UserShippingDAO userShippingDAO;
	
	@Override
	public UserShipping findById(Long id) {
		// TODO Auto-generated method stub
		return userShippingDAO.findById(id).get();
		
	}

	@Override
	public void removeById(Long id) {
		// TODO Auto-generated method stub		
		userShippingDAO.deleteById(id);
		
	}

}
