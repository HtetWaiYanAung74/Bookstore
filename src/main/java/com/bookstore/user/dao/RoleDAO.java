/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.dao;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.user.domain.security.Role;

public interface RoleDAO extends CrudRepository<Role, Integer> {

	Role findByName(String name);
	
}
