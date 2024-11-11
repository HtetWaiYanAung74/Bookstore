package com.bookstore.user.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bookstore.user.domain.Book;
import com.bookstore.user.domain.User;
import com.bookstore.user.service.BookService;
import com.bookstore.user.service.UserService;

@Controller
public class SearchController {

	@Autowired
	private UserService userService;;
	
	@Autowired
	private BookService bookService;
	
	@RequestMapping("/searchByCategory")
	private String searchByCategory(@RequestParam("category") String category, Principal principal, Model model) {
		
		if (principal != null) {
		
			User user = userService.findByUsername(principal.getName());
			model.addAttribute("user", user);
			
		}
		
		String classActiveCategory = "active" + category;
		
		classActiveCategory.replaceAll("\\s+", "");
		classActiveCategory.replaceAll("&", "");
	
		model.addAttribute("classActiveCategory", classActiveCategory);
	
		List<Book> bookList = bookService.findByCategory(category);
		
		if (bookList.isEmpty()) {
			
			model.addAttribute("emptyList", true);
			
			return "bookshelf";
			
		}
		
		model.addAttribute("bookList", bookList);
		
		return "bookshelf";
		
	}
	
	@RequestMapping("/searchBook")
	private String searchBook(@ModelAttribute("keyword") String keyword, Principal principal, Model model) {
		
		if (principal != null) {
			
			User user = userService.findByUsername(principal.getName());
			model.addAttribute("user", user);
			
		}
		
		List<Book> bookList = bookService.blurrySearch(keyword);
        
        if (bookList.isEmpty()) {
        	
            model.addAttribute("emptyList", true);
            return "bookshelf";
            
        }
        
        model.addAttribute("bookList", bookList);
        return "bookshelf";
		
	}
	
}
