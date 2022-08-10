package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.RecommendedBooksPageDto;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.BooksRatingAndPopularityService;
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

		private BooksRatingAndPopularityService booksRatingAndPopularityService;

//		@Autowired
//		public BooksController(
//				BooksRatingAndPopularityService booksRatingAndPopularityService)
//		{
//				this.booksRatingAndPopularityService = booksRatingAndPopularityService;
//		}

		@Autowired
		public BooksController(BookService bookService, BooksRatingAndPopularityService booksRatingAndPopularityService)
		{
				this.booksRatingAndPopularityService = booksRatingAndPopularityService;
				this.bookService = bookService;
		}

		@GetMapping("/author")
		public String booksPageAuthor()
		{//Model model){
				return "/books/author";
		}

		@GetMapping("/popular")
		public String booksPagePopular(Model model)
		{
				model.addAttribute("popularBooks",
						booksRatingAndPopularityService.findPopularsBooks(0,5));
				return "/books/popular";
		}

		@GetMapping("/recent")
		public String booksPageRecent(@RequestParam(value = "from", required = false) String fromDate,
				@RequestParam(value = "to", required = false) String toDate,
				@RequestParam(value = "offset", required = false) Integer offset,
				@RequestParam(value = "limit", required = false) Integer limit,
				Model model)
		{
				if (fromDate != null || toDate != null)
				{
						model.addAttribute("recentResults",
								bookService.findBooksByPubDateBetween(fromDate, toDate, offset, limit));
				}
				else
				{
						model.addAttribute("recentResults",
								bookService.findTopByPubDate(0, 20));
				}
				return "/books/recent";
		}
		//		@GetMapping("/books/recent")
		//		//		http://localhost:8085/books/recent?from=01.07.2022&to=02.08.2022&offset=0&limit=20
		//		@ApiOperation("get books between start and end period")
		//		public ResponseEntity<List<Book>> booksBetweenDates(@RequestParam("from") String fromDate,
		//				@RequestParam("to") String toDate, @RequestParam("offset") Integer offset,
		//				@RequestParam("limit") Integer limit)
		//		{
		//				return ResponseEntity.ok(bookService.findBooksByPubDateBetween(fromDate, toDate, offset, limit));
		//		}

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
