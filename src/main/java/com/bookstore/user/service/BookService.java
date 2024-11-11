/**
 * @author Htet Wai Yan Aung
 */

package com.bookstore.user.service;

import java.util.List;

import com.bookstore.user.domain.Book;

public interface BookService {
	
	Book save(Book book);
	
	List<Book> findAll();
	
	Book findById(Long id);
	
	List<Book> findByCategory(String category);
	
	List<Book> blurrySearch(String title);

}
