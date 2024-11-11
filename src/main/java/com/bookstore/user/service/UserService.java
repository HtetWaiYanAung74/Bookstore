/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.service;

import java.util.Set;

import com.bookstore.user.domain.User;
import com.bookstore.user.domain.UserBilling;
import com.bookstore.user.domain.UserPayment;
import com.bookstore.user.domain.UserShipping;
import com.bookstore.user.domain.security.PasswordResetToken;
import com.bookstore.user.domain.security.UserRole;

public interface UserService {
	
	PasswordResetToken getPasswordResetToken(final String token);
	
	void createPasswordResetTokenForUser(final User user, final String token);

	User findById(Long id);
	
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	User saveUser(User user);
	
	User createUser(User user, Set<UserRole> userRoles);

	void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user);
	
	void updateUserShipping(UserShipping userShipping, User user);
	
	void setUserDefaultPayment(Long userPaymentId, User user);
	
	void setUserDefaultShipping(Long userShippingId, User user);
		
}
