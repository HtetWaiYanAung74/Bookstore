package com.bookstore.user.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.user.domain.BillingAddress;
import com.bookstore.user.domain.CartItem;
import com.bookstore.user.domain.Order;
import com.bookstore.user.domain.Payment;
import com.bookstore.user.domain.ShippingAddress;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.domain.User;
import com.bookstore.user.domain.UserPayment;
import com.bookstore.user.domain.UserShipping;
import com.bookstore.user.service.BillingAddressService;
import com.bookstore.user.service.CartItemService;
import com.bookstore.user.service.OrderService;
import com.bookstore.user.service.PaymentService;
import com.bookstore.user.service.ShippingAddressService;
import com.bookstore.user.service.ShoppingCartService;
import com.bookstore.user.service.UserPaymentService;
import com.bookstore.user.service.UserService;
import com.bookstore.user.service.UserShippingService;
import com.bookstore.user.utility.MailConstructor;
import com.bookstore.user.utility.USConstants;

@Controller
public class CheckoutController {
	
	private ShippingAddress shippingAddress = new ShippingAddress();
	
	private BillingAddress billingAddress = new BillingAddress();
	
	private Payment payment = new Payment();
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@Autowired
	private ShippingAddressService shippingAddressService;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private CartItemService cartItemService;
	
	@Autowired
	private UserShippingService userShippingService;
	
	@Autowired
	private UserPaymentService userPaymentService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private BillingAddressService billingAddressService;
	
	@RequestMapping("/checkout")
	private String checkout(@RequestParam("id") Long shoppingCartId,
			@RequestParam(value = "missingRequiredField",required = false) boolean missingRequiredField,
			Model model,Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		if (shoppingCartId != user.getShoppingCart().getId()) {
			
			return "badRequestPage";
		}
		
		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
		if (cartItemList.size() == 0) {
			model.addAttribute("emptyCart",true);
			return "forward:/shoppingCart/cart";
		}
		
		for(CartItem cartItem : cartItemList) {
			
			if (cartItem.getBook().getInStockNumber() < cartItem.getQty()) {
				
				model.addAttribute("notEnoughStock",true);
				return "forward:/shoppingCart/cart";
			}
			
		}
		
		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		model.addAttribute("userShippingList", userShippingList);
		model.addAttribute("userPaymentList", userPaymentList);
		
		if (userPaymentList.size() == 0) {
			
			model.addAttribute("emptyPaymentList",true);
			
		}else {
			
			model.addAttribute("emptyPaymentList",false);
		}
		
		if (userShippingList.size() == 0) {

			model.addAttribute("emptyShippingList", true);

		} else {

			model.addAttribute("emptyShippingList", false);
		}
		
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		for(UserShipping userShipping : userShippingList) {
			
			if (userShipping.getUserShippingDefault()) {
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
			
		}
		
		for (UserPayment userPayment : userPaymentList) {

			if (userPayment.getDefaultPayment()) {
				paymentService.setByUserPayment(userPayment, payment);
				
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}

		}
		
		model.addAttribute("shippingAddress", shippingAddress);
		model.addAttribute("payment", payment);
		model.addAttribute("cartItemList", cartItemList);
		model.addAttribute("shoppingCart", shoppingCart);
		model.addAttribute("billingAddress", billingAddress);
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		
		model.addAttribute("stateList", stateList);
		
		model.addAttribute("classActiveShipping", true);
		
		if (missingRequiredField) {
			
			model.addAttribute("missingRequiredField",true);
			
		}
		
		return "checkout";
	}
	
	@PostMapping("/checkout")
	private String checkoutPost(@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress,
			@ModelAttribute("payment") Payment payment,
			@ModelAttribute("billingSameAsShipping") String billingSameAsShipping,
			@ModelAttribute("shippingMethod") String shippingMethod,
			Model model,Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		ShoppingCart shoppingCart = user.getShoppingCart();
		
		List<CartItem> cartItemList = shoppingCart.getCartItemList();
		
		model.addAttribute("cartItemList", cartItemList);
		
		if (billingSameAsShipping.equals("true")) {
			
			billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
			billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
			billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
			billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
			billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
			billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
			billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());
		}
		
		if (shippingAddress.getShippingAddressCity().isEmpty() || shippingAddress.getShippingAddressName().isEmpty() || 
			shippingAddress.getShippingAddressState().isEmpty() || shippingAddress.getShippingAddressStreet1().isEmpty() ||
			shippingAddress.getShippingAddressZipcode().isEmpty() || payment.getCardNumber().isEmpty() ||
			payment.getHolderName().isEmpty() || payment.getType().isEmpty() ||
			billingAddress.getBillingAddressCity().isEmpty() || billingAddress.getBillingAddressName().isEmpty() ||
			billingAddress.getBillingAddressState().isEmpty() || billingAddress.getBillingAddressStreet1().isEmpty() ||
			billingAddress.getBillingAddressZipcode().isEmpty()) {
			
			return "redirect:/checkout?id="+shoppingCart.getId()+"&missingRequiredField=true";
			
		}
		
		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod, user);
		
		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order,Locale.ENGLISH));
		
		shoppingCartService.clearShoppingCart(shoppingCart);
		
		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;
		
		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		}else {
			estimatedDeliveryDate = today.plusDays(3);
		}
		
		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);
		
		return "orderSubmittedPage";
		
		
	}
	
	@RequestMapping("/setShippingAddress")
	public String setShippingAddress(@RequestParam("userShippingId") Long userShippingId,
			Principal principal,Model model) {
		User user = userService.findByUsername(principal.getName());
		
		UserShipping userShipping = userShippingService.findById(userShippingId);
		
		if (userShipping.getUser().getId() != user.getId()) {
			
			return "badRequestPage";
		}else {
			
			shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
			List<UserShipping> userShippingList = user.getUserShippingList();
			List<UserPayment> userPaymentList = user.getUserPaymentList();
			
			model.addAttribute("userShippingList", userShippingList);
			model.addAttribute("userPaymentList", userPaymentList);
			
			if (userShippingList.size() == 0) {

				model.addAttribute("emptyShippingList", true);

			} else {

				model.addAttribute("emptyShippingList", false);
			}
			
			if (userPaymentList.size() == 0) {
				
				model.addAttribute("emptyPaymentList",true);
				
			}else {
				
				model.addAttribute("emptyPaymentList",false);
			}
			
			
			ShoppingCart shoppingCart = user.getShoppingCart();
			
			model.addAttribute("shippingAddress", shippingAddress);
			model.addAttribute("payment", payment);
			model.addAttribute("cartItemList", cartItemList);
			model.addAttribute("shoppingCart", shoppingCart);
			model.addAttribute("billingAddress", billingAddress);
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			
			model.addAttribute("stateList", stateList);
			
			model.addAttribute("classActiveShipping", true);
			
			return "checkout";
			
		}
				
	}

}
