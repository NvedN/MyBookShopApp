package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.*;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashSet;
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
				return bookService.getPageOfRecommendedBooks(0, 6).getContent();
		}

		@ModelAttribute("newsBooks")
		public List<Book> newsBooks()
		{
				return bookService.getPageOfNewsBooks(0, 6).getContent();
		}

		@ModelAttribute("popularBooks")
		public List<Book> popularsBooks()
		{
				return bookService.getPageOfRecommendedBooks(0, 6).getContent();
		}

		@GetMapping("/")
		public String mainPage(Model model, SearchWordDto searchWordDto)
		{
				model.addAttribute("searchWordDto", searchWordDto);
				return "index";
		}

		@GetMapping("/books/recommended")
		@ResponseBody
		public RecommendedBooksPageDto getBooksPage(@RequestParam("offset") Integer offset,
				@RequestParam("limit") Integer limit)
		{
				return new RecommendedBooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
		}

		@GetMapping("/books/newsBooks")
		@ResponseBody
		public BooksPageDto getNewsBooksPage(@RequestParam("offset") Integer offset,
				@RequestParam("limit") Integer limit)
		{
				return new BooksPageDto(bookService.getPageOfNewsBooks(offset, limit).getContent());
		}

		@GetMapping("/books/popularBooks")
		@ResponseBody
		public BooksPageDto getPopularBooksPage(@RequestParam("offset") Integer offset,
				@RequestParam("limit") Integer limit)
		{
				System.out.println("---------here?");
				return new BooksPageDto(bookService.getPageOfNewsBooks(offset, limit).getContent());
		}

		@ModelAttribute("tags")
		public HashSet<String> mainPageTags()
		{
				return bookService.getBooksByTag();
		}

		@GetMapping("/tags")
		public String mainPageTags(@RequestParam(value = "tag", required = false) String tag,
				Model model)
		{
				model.addAttribute("tagName",tag);
				model.addAttribute("tagResults", bookService.getPageOfTags(tag, 0, 20).getContent());
				return "/tags/index";
		}


}
