package com.bookstore.user.service;

import com.bookstore.user.domain.UserShipping;

public interface UserShippingService {
	
	UserShipping findById(Long id);
	
	void removeById(Long id);

}
