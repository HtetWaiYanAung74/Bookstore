package com.bookstore.user.service;

import com.bookstore.user.domain.ShippingAddress;
import com.bookstore.user.domain.UserShipping;

public interface ShippingAddressService {
	
	ShippingAddress setByUserShipping(UserShipping userShipping, ShippingAddress shippingAddress);

}
