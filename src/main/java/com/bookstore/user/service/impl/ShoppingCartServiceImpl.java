package com.bookstore.user.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.user.dao.ShoppingCartDAO;
import com.bookstore.user.domain.CartItem;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.service.CartItemService;
import com.bookstore.user.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private ShoppingCartDAO shoppingCartDAO;
	
	@Override
	public ShoppingCart updateShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		
		BigDecimal cartTotal = new BigDecimal(0);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for(CartItem cartItem : cartItemList) {
			
			if(cartItem.getBook().getInStockNumber() > 0) {
				
				cartItemService.updateCartItem(cartItem);
				
				cartTotal = cartTotal.add(cartItem.getSubTotal());
				
			}
			
		}
		
		shoppingCart.setGrandTotal(cartTotal);
		shoppingCartDAO.save(shoppingCart);
		
		return shoppingCart;
	}

	@Override
	public void clearShoppingCart(ShoppingCart shoppingCart) {
		// TODO Auto-generated method stub
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for(CartItem cartItem : cartItemList) {
			
			cartItem.setShoppingCart(null);
			cartItemService.save(cartItem);			
			
		}
		
		shoppingCart.setGrandTotal(new BigDecimal(0));
		shoppingCartDAO.save(shoppingCart);
		
		
	}

}
