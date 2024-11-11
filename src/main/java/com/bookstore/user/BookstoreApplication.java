/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bookstore.user.domain.User;
import com.bookstore.user.domain.security.Role;
import com.bookstore.user.domain.security.UserRole;
import com.bookstore.user.service.UserService;
import com.bookstore.user.utility.SecurityUtility;

@SpringBootApplication
public class BookstoreApplication implements CommandLineRunner {

	@Autowired
	private UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(BookstoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
		User user1 = new User();
		user1.setUsername("theint");
		user1.setEmail("noobtony98@gmail.com");
		user1.setFirstName("theint");
		user1.setLastName("aung");
		
		user1.setPassword(SecurityUtility.passwordEncoder().encode("1234"));
		
		Set<UserRole> userRoles = new HashSet<>();
		
		Role role1 = new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
		
	}

}
