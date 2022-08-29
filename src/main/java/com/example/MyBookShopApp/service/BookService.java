package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookRepository;
import com.example.MyBookShopApp.exceptions.BookstoreApiWrongParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class BookService
{

		private BookRepository bookRepository;

		@Autowired
		public BookService(BookRepository bookRepository)
		{
				this.bookRepository = bookRepository;
		}

		public List<Book> getBooksData()
		{
				return bookRepository.findAll();
		}
		//NEW BOOK SERVICE METHODS

		public List<Book> getBooksByAuthor(String authorName)
		{
				return bookRepository.findBooksByAuthorFirstNameContaining(authorName);
		}

		public List<Book> getBooksByTitle(String title) throws BookstoreApiWrongParameterException
		{
				if ("".equals(title) || title.length() <= 1){
						throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
				}else{
						List<Book> data = bookRepository.findBooksByTitleContaining(title);
						if (data.size() > 0){
								return data;
						}else{
								throw new BookstoreApiWrongParameterException("No data found with specified parameters");
						}
				}
		}

		public List<Book> getBooksWithPriceBetween(Integer min, Integer max)
		{
				return bookRepository.findBooksByPriceOldBetween(min, max);
		}

		public List<Book> getBooksWithPrice(Integer price)
		{
				return bookRepository.findBooksByPriceOldIs(price);
		}

		public List<Book> getBooksWithMaxPrice()
		{
				return bookRepository.getBooksWithMaxDiscount();
		}

		public List<Book> getBestsellers()
		{
				return bookRepository.getBestsellers();
		}

		public Page<Book> getPageOfRecommendedBooks(Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return bookRepository.findAll(nextPage);
		}

		public Page<Book> getPageOfNewsBooks(Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return bookRepository.findAll(nextPage);
		}

		public List<Book> findBooksByPubDateBetween(String fromDate, String toDate, Integer offset, Integer limit)
		{
				LocalDateTime fromDateTime = null;
				LocalDateTime toDateTime = null;
				Pageable nextPage = PageRequest.of(0, 20);
				if (!"".equals(fromDate))
				{
						fromDateTime = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
								.atTime(0, 0, 0, 0);
				}
				if (!"".equals(toDate))
				{
						toDateTime = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
								.atTime(0, 0, 0, 0);
				}
				return bookRepository.findBooksByPubDateBetween(fromDateTime, toDateTime, nextPage).getContent();
		}

		public Page<Book> getPageOfSearchResultBooks(String searchWork, Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return bookRepository.findBookByTitleContaining(searchWork, nextPage);
		}



		public List<Book> findTopByPubDate(Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return bookRepository.findAllByOrderByPubDateDesc(nextPage).getContent();
		}

		public HashSet<String> getBooksByTag()
		{
				HashSet<String> output = new HashSet<>();
				List<Book> allBooks = bookRepository.findAll();
				for(Book book : allBooks){
						output.add(book.getTag());
				}
				return output;
		}

		public Page<Book> getPageOfTags(String tag, Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return bookRepository.findBookByTagContaining(tag, nextPage);
		}

}
