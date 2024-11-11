/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.user.domain.Book;
import com.bookstore.user.domain.Order;
import com.bookstore.user.domain.User;
import com.bookstore.user.domain.UserBilling;
import com.bookstore.user.domain.UserPayment;
import com.bookstore.user.domain.UserShipping;
import com.bookstore.user.domain.security.PasswordResetToken;
import com.bookstore.user.domain.security.Role;
import com.bookstore.user.domain.security.UserRole;
import com.bookstore.user.service.BookService;
import com.bookstore.user.service.OrderService;
import com.bookstore.user.service.UserPaymentService;
import com.bookstore.user.service.UserService;
import com.bookstore.user.service.impl.UserSecurityService;
import com.bookstore.user.utility.MailConstructor;
import com.bookstore.user.utility.SecurityUtility;
import com.bookstore.user.utility.USConstants;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private BookService bookService;
	
	@Autowired
	private UserPaymentService userPaymentService;	

	
	@Autowired
	private OrderService orderService;
	
	@RequestMapping("/")
	private String home() {
		
		return "index";
		
	}
	
	@RequestMapping("/index")
	private String index() {
		
		return "redirect:/";
		
	}
	
	@RequestMapping("/login")
	private String login(Model model) {
		
		model.addAttribute("classActiveLogin",true);
		return "myAccount";
		
	}
	
//	@RequestMapping(value = "/newUser", method = RequestMethod.POST)
	@PostMapping("/newUser")
	public String newUserPost(HttpServletRequest request, 
			@ModelAttribute("username") String username, @ModelAttribute("email") String email, Model model) {
		
		model.addAttribute("classActiveNewAccount", true);
		
		if(userService.findByUsername(username) != null) {
			
			model.addAttribute("usernameExists", true);
			
			return "myAccount";
			
		}
		
		if(userService.findByEmail(email) != null) {
			
			model.addAttribute("emailExists", true);
			
			return "myAccount";
			
		}
		
		User user = new User();
		user.setEmail(email);
		user.setUsername(username);
		
		String password = SecurityUtility.randomPassword();
		
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		
		user.setPassword(encryptedPassword);
		
		Role role = new Role();
		
		role.setRoleId(1);
		role.setName("ROLE_USER");
		
		Set<UserRole> userRoles = new HashSet<UserRole>();
		
		userRoles.add(new UserRole(user, role));
		
		userService.createUser(user, userRoles);
		
		String token = UUID.randomUUID().toString();
		
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + 
				request.getContextPath();
		
		SimpleMailMessage sentEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, password, "Le's Bookstore - New User");
		
		mailSender.send(sentEmail);
		
		model.addAttribute("emailSent", true);
		
		return "myAccount";
		
	}
	
	@RequestMapping("/newUser")
	public String newUser(Locale locale, @RequestParam(name = "token", required = true) String token,
			Model model) {
		
		PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);
		
		if (passwordResetToken == null) {
		
			String message = "Invalid Token.";
			model.addAttribute("message", message);
			return "redirect:/badRequest";
			
		}
		
		User user = passwordResetToken.getUser();
		
		String email = user.getEmail();
		
		UserDetails userDetails = userSecurityService.loadUserByUsername(email);
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		model.addAttribute("user", user);
		
		model.addAttribute("classActiveEdit", true);
		
		return "myProfile";
		
	}
	
	@PostMapping("/updateUserInfo")
	public String updateUserInfo(@ModelAttribute("user") User user,
			@ModelAttribute("currentPassword") String currentPassword,
			@ModelAttribute("newPassword") String newPassword,
			Model model) throws Exception {
		
		User currentUser = userService.findById(user.getId());
		
		if (currentUser == null) {
			
			throw new Exception("User Not Found");
			
		}
		
		if (userService.findByUsername(user.getUsername()) != null) {
			
			if (userService.findByUsername(user.getUsername()).getId() != currentUser.getId()) {
				
				model.addAttribute("usernameExists", true);
				
				model.addAttribute("classActiveEdit", true);
				
				return "myProfile";
				
			}
			
		}
		
		if (userService.findByEmail(user.getEmail()) != null) {
			
			if (userService.findByEmail(user.getEmail()).getId() != currentUser.getId()) {
				
				model.addAttribute("emailExists", true);
				
				model.addAttribute("classActiveEdit", true);
				
				return "myProfile";
				
			}
			
		}
		
		if (newPassword != null && !newPassword.isEmpty() && !newPassword.equals("")) {
			
			BCryptPasswordEncoder passwordEncoder = SecurityUtility.passwordEncoder();
			
			String dbPassword = currentUser.getPassword();
			
			if (passwordEncoder.matches(currentPassword, dbPassword)) {
				
				currentUser.setPassword(passwordEncoder.encode(newPassword));
				
			} else {
				
				model.addAttribute("incorrectPassword", true);
				return "myProfile";
				
			}
			
		}
		
		currentUser.setFirstName(user.getFirstName());
		currentUser.setLastName(user.getLastName());
		currentUser.setUsername(user.getUsername());
		currentUser.setEmail(user.getEmail());
		currentUser.setPhone(user.getPhone());
		
		userService.saveUser(currentUser);
		
		model.addAttribute("updateUserInfo", true);
		
		model.addAttribute("user", currentUser);
		
		model.addAttribute("classActiveEdit", true);
		
		UserDetails userDetails = userSecurityService.loadUserByUsername(currentUser.getEmail());
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "myProfile";
		
	}
	
	@RequestMapping("/myProfile")
	public String myProfile(Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		model.addAttribute("user", user);
		
		model.addAttribute("classActiveEdit", true);
		
		model.addAttribute("orderList", user.getOrderList());
		
		return "myProfile";
		
	}
	
	@PostMapping("/forgetPassword")
	public String forgetPassword(HttpServletRequest request, @ModelAttribute("email") String email, Model model) {
		
		model.addAttribute("classActiveForgetPassword", true);
		
		User user = userService.findByEmail(email);
		
		if (user == null) {
			
			model.addAttribute("emailNotExist", true);
			return "myAccount";
		}
		
		String password = SecurityUtility.randomPassword();
		
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		
		user.setPassword(encryptedPassword);
		
		userService.saveUser(user);
		
		String token = UUID.randomUUID().toString();
		
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		
		SimpleMailMessage sentEmail = mailConstructor.constructResetTokenEmail(appUrl, request.getLocale(), token, user, encryptedPassword, "Le's Bookstore - Password Reset");
		
		mailSender.send(sentEmail);
		
		model.addAttribute("forgetPasswordEmailSent", true);
		
		return "myAccount";
		
	}
	
	@RequestMapping("/bookshelf")
	public String bookshelf(Model model, Principal principal) {
		
		if (principal != null) {
			
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		
		}
		
		List<Book> bookList = bookService.findAll();
		
		model.addAttribute("bookList", bookList);
		
		model.addAttribute("activeAll", true);
		
		return "bookshelf";
		
	}
	
	@RequestMapping("/bookDetail")
	public String bookDetail(@RequestParam("id") Long id, Model model, Principal principal) {
		
		if (principal != null) {
			
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user", user);
		
		}

		Book book = bookService.findById(id);
		
		model.addAttribute("book", book);
		
		List<Integer> qtyList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		
		model.addAttribute("qtyList", qtyList);
		
		model.addAttribute("qty", 1);
		
		return "bookDetail";
		
	}
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		
		model.addAttribute("user", user);
		model.addAttribute("userBilling", userBilling);
		model.addAttribute("userPayment", userPayment);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("addNewCreditCard", true);
		
		return "myProfile";
		
	}
		
	@PostMapping("/addNewCreditCard")
	public String addNewCreditCard(@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling, Principal principal, Model model
			) {
		
		User user = userService.findByUsername(principal.getName());
		
		userService.updateUserBilling(userBilling, userPayment, user);
		
		model.addAttribute("user", user);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("listOfCreditCards", true);
				
		return "myProfile";
		
	}
	
	@RequestMapping("/listOfCreditCards")
	private String listOfCreditCards(Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		model.addAttribute("user", user);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("listOfCreditCards", true);
		
		return "myProfile";
		
	}
	
	@RequestMapping("/updateCreditCard")
	public String updateCreditCard(@RequestParam("id") Long userPaymentId,
			Principal principal, Model model) {
		
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		
		if(user.getId() != userPayment.getUser().getId()) {
			
			return "badRequestPage";
			
		} else {
			
			List<String> stateList = USConstants.listOfUSStatesCode;
			Collections.sort(stateList);
			
			model.addAttribute("user", user);
			model.addAttribute("stateList", stateList);
			model.addAttribute("userBilling", userPayment.getUserBilling());
			model.addAttribute("userPayment", userPayment);
			model.addAttribute("classActiveBilling", true);
			model.addAttribute("addNewCreditCard", true);
			
		}	
		
		return "myProfile";
		
	}
	
	@RequestMapping("/removeCreditCard")
	public String removeCreditCard(@RequestParam("id") Long userPaymentId, Principal principal, Model model) {
		
		User user = userService.findByUsername(principal.getName());
		UserPayment userPayment = userPaymentService.findById(userPaymentId);
		
		if(user.getId() != userPayment.getUser().getId()) {
						
			return "badRequestPage";
			
		}
		
		userPaymentService.removeById(userPaymentId);
		
		model.addAttribute("user", user);
		model.addAttribute("classActiveBilling", true);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("listOfCreditCards", true);
		
		return "myProfile";
		
	}
	
	@RequestMapping("/setDefaultPayment")
	public String setDefaultPayment(@ModelAttribute("defaultUserPaymentId") Long userPaymentId, Principal principal, Model model) {
		
		User user = userService.findByUsername(principal.getName());
		
		userService.setUserDefaultPayment(userPaymentId, user);
		
		model.addAttribute("user",user);
		model.addAttribute("classActiveBilling",true);
		model.addAttribute("userPaymentList", user.getUserPaymentList());
		model.addAttribute("listOfCreditCards", true);
		
		return "myProfile";
		
	}
	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		UserShipping userShipping = new UserShipping();
		
		List<String> stateList = USConstants.listOfUSStatesCode;
		Collections.sort(stateList);
		
		model.addAttribute("user", user);
		model.addAttribute("userShipping", userShipping);
		model.addAttribute("stateList", stateList);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("addNewShippingAddress", true);
		
		return "myProfile";
		
	}
	
	@PostMapping("/addNewShippingAddress")
	public String addNewShippingAddress(@ModelAttribute("userShipping") UserShipping userShipping, 
			Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		userService.updateUserShipping(userShipping, user);
		
		model.addAttribute("user", user);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
		
	}
	
	@RequestMapping("/listOfShippingAddresses")
	private String listOfShippingAddresses(Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		model.addAttribute("user", user);
		model.addAttribute("classActiveShipping", true);
		model.addAttribute("userShippingList", user.getUserShippingList());
		model.addAttribute("listOfShippingAddresses", true);
		
		return "myProfile";
		
	}
	
	@RequestMapping("/orderDetail")
	public String orderDetail(@RequestParam("id") Long orderId, Model model, Principal principal) {
		
		User user = userService.findByUsername(principal.getName());
		
		Order order = orderService.findById(orderId);
		
		if (order.getUser().getId() != user.getId()) {
			
			return "badRequestPage";
			
		}
		
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("classActiveOrders", true);
		model.addAttribute("displayOrderDetail", true);
		
		return "myProfile";
		
	}
	
}