package com.bookstore.user.service;

import com.bookstore.user.domain.BillingAddress;
import com.bookstore.user.domain.Order;
import com.bookstore.user.domain.Payment;
import com.bookstore.user.domain.ShippingAddress;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.domain.User;

public interface OrderService {

	Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress,
			Payment payment, String shippingMethod, User user);
	
	Order findById(Long id);
	
}
