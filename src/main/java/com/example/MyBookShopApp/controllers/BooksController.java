package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController
{
		private BookService bookService ;

		@Autowired
		public BooksController(BookService bookService) {
				this.bookService = bookService;
		}

		@GetMapping("/author")
		public String booksPageAuthor()
		{//Model model){
				return "/books/author";
		}

		@GetMapping("/popular")
		public String booksPagePopular()
		{//Model model){
				return "/books/popular";
		}

		@GetMapping("/recent")
		public String booksPageRecent()
		{//Model model){
				return "/books/recent";
		}

		@GetMapping("/slug")
		public String booksPageSlug()
		{//Model model){
				return "/books/slug";
		}

		@ModelAttribute("booksList")
		public List<Book> bookList(){
				return bookService.getBooksData();
		}
		@GetMapping("/books/popular")
		public String recentBookPage(){
				return "books/popular";
		}

}
