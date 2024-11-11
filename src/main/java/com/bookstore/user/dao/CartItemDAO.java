package com.bookstore.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.user.domain.CartItem;
import com.bookstore.user.domain.ShoppingCart;

public interface CartItemDAO extends CrudRepository<CartItem, Long>{

	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	
	
}
