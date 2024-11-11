package com.bookstore.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.user.domain.BillingAddress;
import com.bookstore.user.domain.Order;
import com.bookstore.user.domain.Payment;
import com.bookstore.user.domain.ShippingAddress;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.domain.User;

public interface OrderDAO extends CrudRepository<Order, Long> {

	
	
}
