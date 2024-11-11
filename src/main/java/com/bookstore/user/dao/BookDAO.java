/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bookstore.user.domain.Book;


public interface BookDAO extends CrudRepository<Book, Long> {
	
	List<Book> findAll();

	List<Book> findByCategory(String category);

	List<Book> findByTitleContaining(String title);

}
