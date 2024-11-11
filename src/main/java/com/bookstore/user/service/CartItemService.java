package com.bookstore.user.service;

import java.util.List;

import com.bookstore.user.domain.Book;
import com.bookstore.user.domain.CartItem;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.domain.User;

public interface CartItemService {
	
	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);
	
	CartItem updateCartItem(CartItem cartItem);
	
	CartItem addBookToCartItem(Book book, User user, Integer qty);
	
	CartItem findById(Long id);
	
	CartItem save(CartItem cartItem);
	
	void removeCartItem(CartItem cartItem);

}
