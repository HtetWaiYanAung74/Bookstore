/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.service.impl;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.user.dao.PasswordResetTokenDAO;
import com.bookstore.user.dao.RoleDAO;
import com.bookstore.user.dao.UserDAO;
import com.bookstore.user.dao.UserPaymentDAO;
import com.bookstore.user.dao.UserShippingDAO;
import com.bookstore.user.domain.ShoppingCart;
import com.bookstore.user.domain.User;
import com.bookstore.user.domain.UserBilling;
import com.bookstore.user.domain.UserPayment;
import com.bookstore.user.domain.UserShipping;
import com.bookstore.user.domain.security.PasswordResetToken;
import com.bookstore.user.domain.security.UserRole;
import com.bookstore.user.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private RoleDAO roleDAO;
	
	@Autowired
	private PasswordResetTokenDAO passwordResetTokenDAO;

	@Autowired
	private UserPaymentDAO userPaymentDAO; 
	
	@Autowired
	private UserShippingDAO userShippingDAO; 
	
	@Override
	public User findByUsername(String username) {
		return userDAO.findByUsername(username);
	}

	@Override
	public User findByEmail(String email) {
		return userDAO.findByEmail(email);
	}

	@Override
	public User saveUser(User user) {
		return userDAO.save(user);
	}

	@Override
	public User createUser(User user, Set<UserRole> userRoles) {
		
		User localUser = userDAO.findByEmail(user.getEmail());
		
		if (localUser != null) {
			LOG.info("user {} already exists. Nothing will be done.");
		} else {
			
			for(UserRole ur : userRoles) {
				roleDAO.save(ur.getRole());
			}
			
			//One to Many Connect
			user.getUserRoles().addAll(userRoles);
			
			ShoppingCart shoppingCart = new ShoppingCart();
			shoppingCart.setUser(user);
			user.setShoppingCart(shoppingCart);
			
			localUser = userDAO.save(user);
			
		}
		
		return localUser;
						
	}

	@Override
	public PasswordResetToken getPasswordResetToken(String token) {
		
		return passwordResetTokenDAO.findByToken(token);
	
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) {
		
		final PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
		
		passwordResetTokenDAO.save(passwordResetToken);
		
	}

	@Override
	public User findById(Long id) {
		
		return userDAO.findById(id).get();
	
	}

	@Override
	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user) {
		// TODO Auto-generated method stub
		
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		if(userPayment.getId() == null) {
			
			for(UserPayment payment : userPaymentList) {
					
				payment.setDefaultPayment(false);
				userPaymentDAO.save(payment);
				
			}
			
		}
		 
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		
		userBilling.setUserPayment(userPayment);
		
		user.getUserPaymentList().add(userPayment);
		
		userDAO.save(user);
		
		if(userPayment.getId() != null) {		

			for(UserPayment payment : userPaymentList) {
				
				if(payment.getId() != userPayment.getId()) {
					
					payment.setDefaultPayment(false);
					userPaymentDAO.save(payment);
					
				}
				
			}
						
		}
	}

	@Override
	public void updateUserShipping(UserShipping userShipping, User user) {
		// TODO Auto-generated method stub
		
		List<UserShipping> userShippingList = user.getUserShippingList();
		
		for(UserShipping shipping : userShippingList) {
			
			shipping.setUserShippingDefault(false);
			
			userShippingDAO.save(shipping);
			
		}
		
		userShipping.setUser(user);
		
		user.getUserShippingList().add(userShipping);
		
		userShipping.setUserShippingDefault(true);
		
		userDAO.save(user);
		
	}

	@Override
	public void setUserDefaultPayment(Long userPaymentId, User user) {
		// TODO Auto-generated method stub
		
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		
		for(UserPayment userPayment : userPaymentList) {
			
			if (userPayment.getId() == userPaymentId) {
				
				userPayment.setDefaultPayment(true);
				userPaymentDAO.save(userPayment);
				
			} else {
				
				userPayment.setDefaultPayment(false);
				userPaymentDAO.save(userPayment);
				
			}
			
		}
		
	}

	@Override
	public void setUserDefaultShipping(Long userShippingId, User user) {
		// TODO Auto-generated method stub
		
		List<UserShipping> userShippingList = user.getUserShippingList();
		
		for(UserShipping userShipping : userShippingList) {
			
			if (userShipping.getId() == userShippingId) {
				
				userShipping.setUserShippingDefault(true);
				userShippingDAO.save(userShipping);
			
			} else {
				
				userShipping.setUserShippingDefault(false);
				userShippingDAO.save(userShipping);
				
			}
			
		}
		
	}

}
