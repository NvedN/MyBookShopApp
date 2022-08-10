package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		public List<Book> getBooksByTitle(String title)
		{
				return bookRepository.findBooksByTitleContaining(title);
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
				System.out.println(
						"---------bookRepository.findBooksByPubDateBetween(fromDateTime, toDateTime,nextPage).getContent() = "
								+ bookRepository.findBooksByPubDateBetween(fromDateTime, toDateTime, nextPage).getContent());
				System.out.println(
						"---------bookRepository.size = " + bookRepository.findBooksByPubDateBetween(fromDateTime, toDateTime,
								nextPage).getContent().size());
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
				//        2015-04-11 00:00:00.000000
				//        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
				//        List<Book> findedBooks = bookRepository.findAll();
				//        System.out.println("----------fineddeBooks = - " + findedBooks);
				//        for (Book book : findedBooks){
				//            System.out.println("--------book= " + book);
				//            LocalDateTime bookPubDate = book.getPubDate();
				//            System.out.println("------bookPubDate = " + bookPubDate);
				//        }
				//        System.out.println("--------LocalDateTimeNow = " + LocalDateTime.now());
				List<Book> booksList = bookRepository.findTopByOrderByPubDateDesc(nextPage).getContent();
				for (Book book : booksList)
				{
						System.out.println("-----book = " + book);
						System.out.println("-----bookPubDate = " + book.getPubDate());
				}
				System.out.println("----------booksList = " + booksList);
				return bookRepository.findAllByOrderByPubDateDesc(nextPage).getContent();
		}
}
