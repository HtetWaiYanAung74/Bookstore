package com.bookstore.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.user.domain.ShoppingCart;

public interface ShoppingCartDAO extends CrudRepository<ShoppingCart, Long> {

	
	
}
