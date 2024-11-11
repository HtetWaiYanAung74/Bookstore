package com.bookstore.user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.user.domain.Book;
import com.bookstore.user.domain.CartItem;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.domain.User;
import com.bookstore.user.service.BookService;
import com.bookstore.user.service.CartItemService;
import com.bookstore.user.service.ShoppingCartService;
import com.bookstore.user.service.UserService;

@Controller
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@RequestMapping("/cart")
	public String shoppingCart(Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		
		shoppingCartService.updateShoppingCart(shoppingCart);
		
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		
		return "shoppingCart";
		
	}
	
	@PostMapping("/addItem")
	public String addItem(@ModelAttribute("book") Book book, @ModelAttribute("qty") Integer qty,
			Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		book = bookService.findById(book.getId());
		
		if(qty > book.getInStockNumber()) {
			
			model.addAttribute("notEnoughStock", true);
			
			return "forward:/bookDetail?id=" + book.getId();
			
		}
		
		cartItemService.addBookToCartItem(book, user, qty);
		
		model.addAttribute("addBookSuccess", true);
		
		return "forward:/bookDetail?id=" + book.getId();
		
	}
	
	@PostMapping("/updateCartItem")
	public String updateCartItem(@ModelAttribute("id") Long id, @ModelAttribute("qty") Integer qty) {
		
		CartItem cartItem = cartItemService.findById(id);
		
		cartItem.setQty(qty);
		
		cartItemService.updateCartItem(cartItem);
		
		return "forward:/shoppingCart/cart";
		
	}
	
	@RequestMapping("/removeItem")
	public String removeCartItem(@RequestParam("id") Long id) {
		
		CartItem cartItem = cartItemService.findById(id);
		
		cartItemService.removeCartItem(cartItem);
		
		return "forward:/shoppingCart/cart";
		
	}
}
