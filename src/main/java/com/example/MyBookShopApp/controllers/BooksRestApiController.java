package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.ApiResponse;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.exceptions.BookstoreApiWrongParameterException;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
@Api(description = "book data api")
public class BooksRestApiController
{

		private final BookService bookService;

		@Autowired
		public BooksRestApiController(BookService bookService)
		{
				this.bookService = bookService;
		}

		@GetMapping("/books/by-author")
		@ApiOperation("operation to get book list of bookshop by passed author first name")
		public ResponseEntity<List<Book>> booksByAuthor(@RequestParam("author") String authorName)
		{
				return ResponseEntity.ok(bookService.getBooksByAuthor(authorName));
		}

		@GetMapping("/books/by-title")
		@ApiOperation("get books by title")
		public ResponseEntity<ApiResponse<Book>> booksByTitle(@RequestParam("title") String title) throws
				BookstoreApiWrongParameterException
		{
				ApiResponse<Book> response = new ApiResponse<>();
				List<Book> data = bookService.getBooksByTitle(title);
				response.setDebugMessage("successful request");
				response.setMessage("data size : " + data.size() + " elements");
				response.setStatus(HttpStatus.OK);
				response.setTimeStamp(LocalDateTime.now());
				response.setData(data);
				return ResponseEntity.ok(response);
		}

		@GetMapping("/books/by-price-range")
		@ApiOperation("get books by price range from min price to max price")
		public ResponseEntity<List<Book>> priceRangeBookss(@RequestParam("min") Integer min,
				@RequestParam("max") Integer max)
		{
				return ResponseEntity.ok(bookService.getBooksWithPriceBetween(min, max));
		}

		@GetMapping("/books/with-max-discount")
		@ApiOperation("get list of book with max price")
		public ResponseEntity<List<Book>> maxPriceBooks()
		{
				return ResponseEntity.ok(bookService.getBooksWithMaxPrice());
		}

		@GetMapping("/books/bestsellers")
		@ApiOperation("get bestseller book (which is_bestseller = 1)")
		public ResponseEntity<List<Book>> bestSellerBooks()
		{
				return ResponseEntity.ok(bookService.getBestsellers());
		}

		@GetMapping("/books/recent")
//		http://localhost:8085/books/recent?from=01.07.2022&to=02.08.2022&offset=0&limit=20
		@ApiOperation("get books between start and end period")
		public ResponseEntity<List<Book>> booksBetweenDates(@RequestParam("from") String fromDate,
				@RequestParam("to") String toDate, @RequestParam("offset") Integer offset,
				@RequestParam("limit") Integer limit)
		{
				return ResponseEntity.ok(bookService.findBooksByPubDateBetween(fromDate, toDate, offset, limit).getContent());
		}



		@ExceptionHandler(MissingServletRequestParameterException.class)
		public ResponseEntity<ApiResponse<Book>> handleMissingServletRequestParameterException(Exception exception){
				return  new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST,"Missing required parameters", exception),
						HttpStatus.BAD_REQUEST);
		}


		@ExceptionHandler(BookstoreApiWrongParameterException.class)
		public ResponseEntity<ApiResponse<Book>> handleBookstoreApiWrongParameterException(Exception exception){
				return  new ResponseEntity<>(new ApiResponse<Book>(HttpStatus.BAD_REQUEST,"Nad parameter value...", exception),
						HttpStatus.BAD_REQUEST);
		}
}
