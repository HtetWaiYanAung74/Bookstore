package com.bookstore.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bookstore.user.dao.BookDAO;
import com.bookstore.user.domain.Book;
import com.bookstore.user.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	@Autowired
	private BookDAO bookDAO;

	@Override
	public Book save(Book book) {
		// TODO Auto-generated method stub
		return bookDAO.save(book);
	}
	
	@Override
	public List<Book> findAll() {
		// TODO Auto-generated method stub
		
		List<Book> bookList = bookDAO.findAll();
		
		List<Book> activeBookList = new ArrayList<>();
		
		for(Book book : bookList) {
			
			if(book.getActive()) {
				
				activeBookList.add(book);
				
			}
			
		}
		
		return activeBookList;
	}

	@Override
	public Book findById(Long id) {
		// TODO Auto-generated method stub		
		return bookDAO.findById(id).get();
	}

	@Override
	public List<Book> findByCategory(String category) {
		// TODO Auto-generated method stub
		
		List<Book> bookList = bookDAO.findByCategory(category);
		
		List<Book> activeBookList = new ArrayList<>();
		
		for(Book book : bookList) {
			
			if(book.getActive()) {
				
				activeBookList.add(book);
				
			}
			
		}
		
		return activeBookList;
	}

	@Override
	public List<Book> blurrySearch(String title) {
		// TODO Auto-generated method stub
		
		List<Book> bookList = bookDAO.findByTitleContaining(title);
		
		List<Book> activeBookList = new ArrayList<>();
		
		for(Book book : bookList) {
			
			if (book.getActive()) {
				
				activeBookList.add(book);
				
			}
		}
		
		return activeBookList;
		
	}
	
}
