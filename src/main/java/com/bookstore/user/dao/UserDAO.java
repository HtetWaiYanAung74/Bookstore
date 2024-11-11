/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.user.domain.User;

public interface UserDAO extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
	User findByEmail(String email);
	
}
