package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.RecommendedBooksPageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MainPageController
{

		private final BookService bookService;

		@Autowired
		public MainPageController(BookService bookService)
		{
				this.bookService = bookService;
		}

		@ModelAttribute("recommendedBooks")
		public List<Book> recommendedBooks()
		{
				System.out.println("----------START RECOMMENDED BOOKS !!!");
				return bookService.getPageOfRecommendedBooks(0, 6).getContent();
		}

		@GetMapping("/")
		public String mainPage(Model model)
		{
				return "index";
		}



		@GetMapping("/books/recommended")
		@ResponseBody
		public RecommendedBooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
				@RequestParam("limit") Integer limit) {
				System.out.println("---------START LIMIT AND OFFSET");
				return new RecommendedBooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
		}

}
