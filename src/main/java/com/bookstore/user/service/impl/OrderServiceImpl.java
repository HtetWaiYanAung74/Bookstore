package com.bookstore.user.service.impl;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.user.dao.OrderDAO;
import com.bookstore.user.domain.BillingAddress;
import com.bookstore.user.domain.Book;
import com.bookstore.user.domain.CartItem;
import com.bookstore.user.domain.Order;
import com.bookstore.user.domain.Payment;
import com.bookstore.user.domain.ShippingAddress;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.domain.User;
import com.bookstore.user.service.CartItemService;
import com.bookstore.user.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderDAO orderDAO;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Override
	public Order createOrder(ShoppingCart shoppingCart, ShippingAddress shippingAddress, BillingAddress billingAddress,
			Payment payment, String shippingMethod, User user) {
		// TODO Auto-generated method stub
		
		Order order = new Order();
		order.setBillingAddress(billingAddress);
		order.setShippingAddress(shippingAddress);
		order.setOrderStatus("created");
		order.setPayment(payment);
		order.setShippingMethod(shippingMethod);
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		for(CartItem cartItem : cartItemList) {
			
			Book book = cartItem.getBook();
			cartItem.setOrder(order);
			book.setInStockNumber(book.getInStockNumber() - cartItem.getQty());
			
		}
		
		order.setCartItemList(cartItemList);
		order.setOrderDate(Calendar.getInstance().getTime());
		order.setOrderTotal(shoppingCart.getGrandTotal());
		
		shippingAddress.setOrder(order);
		billingAddress.setOrder(order);
		payment.setOrder(order);
		order.setUser(user);
		order = orderDAO.save(order);
		
		return order;
	}

	@Override
	public Order findById(Long id) {
		// TODO Auto-generated method stub
		return orderDAO.findById(id).get();
	}

}
