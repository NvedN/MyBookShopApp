package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.RecommendedBooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BooksController
{

		private BookService bookService;

		@Autowired
		public BooksController(BookService bookService)
		{
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
		public String booksPageRecent(Model model)
		{//Model model){
				System.out.println("------start to filling model in recent page");
				model.addAttribute("recentResults",
						bookService.findTopByPubDate(0, 20));
				return "/books/recent";
		}

		@GetMapping("/slug")
		public String booksPageSlug()
		{//Model model){
				return "/books/slug";
		}

		@ModelAttribute("booksList")
		public List<Book> bookList()
		{
				return bookService.getBooksData();
		}

		@GetMapping("/books/popular")
		public String recentBookPage()
		{
				return "books/popular";
		}

		@GetMapping("/books/news")
		@ResponseBody
		public RecommendedBooksPageDto getNewsBooks(@RequestParam("offset") Integer offset,
				@RequestParam("limit") Integer limit, Model model)
		{
				return new RecommendedBooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
		}

		@ModelAttribute("bookPageDto")
		public BooksPageDto getNextNewsPage()
		{
				return new BooksPageDto();
		}



}
