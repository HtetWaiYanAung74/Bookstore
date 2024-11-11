package com.bookstore.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.user.domain.BookToCartItem;
import com.bookstore.user.domain.CartItem;

public interface BookToCartItemDAO extends CrudRepository<BookToCartItem, Long> {

	void deleteByCartItem(CartItem cartItem);
	
}
