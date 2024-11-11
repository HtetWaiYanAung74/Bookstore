package com.bookstore.user.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bookstore.user.dao.BookToCartItemDAO;
import com.bookstore.user.dao.CartItemDAO;
import com.bookstore.user.domain.Book;
import com.bookstore.user.domain.BookToCartItem;
import com.bookstore.user.domain.CartItem;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.domain.User;
import com.bookstore.user.service.CartItemService;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

	@Autowired
	private CartItemDAO cartItemDAO;
	
	@Autowired
	private BookToCartItemDAO bookToCartItemDAO;
	
	@Override
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		return cartItemDAO.findByShoppingCart(shoppingCart);
	}

	@SuppressWarnings("deprecation")
	@Override
	public CartItem updateCartItem(CartItem cartItem) {
		// TODO Auto-generated method stub
		
		BigDecimal bigDecimal = new BigDecimal(cartItem.getBook().getOurPrice()).
				multiply(new BigDecimal(cartItem.getQty()));
		
		bigDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		
		cartItem.setSubTotal(bigDecimal);
		
		cartItemDAO.save(cartItem);
		
		return cartItem;
		
	}

	@Override
	public CartItem addBookToCartItem(Book book, User user, Integer qty) {
		// TODO Auto-generated method stub
		
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for(CartItem cartItem : cartItemList) {
			
			if(book.getId() == cartItem.getBook().getId()) {
				
				cartItem.setQty(cartItem.getQty() + qty);
				cartItem.setSubTotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(cartItem.getQty())));
				cartItemDAO.save(cartItem);
				
				return cartItem;
				
			}
			
		}
		
		CartItem cartItem = new CartItem();
		
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setBook(book);
		cartItem.setQty(qty);
		cartItem.setSubTotal(new BigDecimal(book.getOurPrice()).multiply(new BigDecimal(qty)));
	
		cartItem = cartItemDAO.save(cartItem);
		
		BookToCartItem bookToCartItem = new BookToCartItem();
		
		bookToCartItem.setBook(book);
		bookToCartItem.setCartItem(cartItem);
		bookToCartItemDAO.save(bookToCartItem);
		
		return cartItem;
		
	}

	@Override
	public CartItem findById(Long id) {
		// TODO Auto-generated method stub
		return cartItemDAO.findById(id).get();
	}

	@Override
	public CartItem save(CartItem cartItem) {
		// TODO Auto-generated method stub
		return cartItemDAO.save(cartItem);
	}

	@Override
	public void removeCartItem(CartItem cartItem) {
		// TODO Auto-generated method stub
		
		bookToCartItemDAO.deleteByCartItem(cartItem);
		
		cartItemDAO.delete(cartItem);
		
	}

}
