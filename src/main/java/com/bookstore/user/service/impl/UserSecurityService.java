/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bookstore.user.dao.UserDAO;
import com.bookstore.user.domain.User;

@Service
public class UserSecurityService implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		User user = userDAO.findByEmail(email);
		
		if(user == null) {	
			throw new UsernameNotFoundException("Email not found");
		}
		
		return user;
	}

}
