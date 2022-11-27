package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.google.api.books.Item;
import com.example.MyBookShopApp.data.google.api.books.Root;
import com.example.MyBookShopApp.data.repository.AuthorRepository;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.exceptions.BookstoreApiWrongParameterException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class BookService
{

		private final BookRepository bookRepository;

		private RestTemplate restTemplate;

		private final AuthorRepository  authorRepository;

		@Autowired
		public BookService(BookRepository bookRepository, RestTemplate restTemplate,
				AuthorRepository authorRepository)
		{
				this.bookRepository = bookRepository;
				this.restTemplate = restTemplate;
			this.authorRepository = authorRepository;
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
				if ("".equals(title) || title.length() <= 1)
				{
						throw new BookstoreApiWrongParameterException("Wrong values passed to one or more parameters");
				}
				else
				{
						List<Book> data = bookRepository.findBooksByTitleContaining(title);
						if (data.size() > 0)
						{
								return data;
						}
						else
						{
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

		public Page<Book> findBooksByPubDateBetween(String fromDate, String toDate, Integer offset, Integer limit)
		{
				LocalDateTime fromDateTime = null;
				LocalDateTime toDateTime = null;
				Pageable nextPage = PageRequest.of(0, 5);
				if (!"".equals(fromDate))
				{
						fromDateTime = LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
								.atTime(0, 0, 0, 0);
				}
				if (!"".equals(toDate))
				{
						toDateTime = LocalDate.parse(toDate, DateTimeFormatter.ofPattern("dd.MM.yyyy"))
								.atTime(0, 0, 0, 0).plusDays(1);
				}
				return bookRepository.findAllByPubDateBetween(fromDateTime, toDateTime, nextPage);
		}

		public Page<Book> getPageOfSearchResultBooks(String searchWork, Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return bookRepository.findBookByTitleContaining(searchWork, nextPage);
		}

		public List<Book> findTopByPubDate(Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return bookRepository.findBooksByPubDateBetween(LocalDateTime.of(2019, 01, 01, 00, 00, 00, 00),
						LocalDateTime.of(2020, 12, 31, 00, 00, 00, 00), nextPage).getContent();
		}

		public HashSet<String> getBooksByTag()
		{
				HashSet<String> output = new HashSet<>();
				List<Book> allBooks = bookRepository.findAll();
				for (Book book : allBooks)
				{
						output.add(book.getTag());
				}
				return output;
		}

		public Page<Book> getPageOfTags(String tag, Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return bookRepository.findBookByTagContaining(tag, nextPage);
		}

		public List<BookReviewEntity> bookReviewEntityList(Book book)
		{
				return book.getBookReviewEntityList();
		}

		@Value("${google.books.api.key}")
		private String apiKey;

		public List<Book> getPageOfGoogleBooksApiSearchResult(String searchWord, Integer offset, Integer limit)
		{
				String REQUEST_URL = "https://www.googleapis.com/books/v1/volumes" +
						"?q=" + searchWord +
						"&key=" + apiKey +
						"&filter=paid-ebooks" +
						"&startIndex=" + offset +
						"&maxResult=" + limit;
				Root root = restTemplate.getForEntity(REQUEST_URL, Root.class).getBody();
				ArrayList<Book> list = new ArrayList<>();
				if (root != null)
				{
						for (Item item : root.getItems())
						{
								Book book = new Book();
								if (item.getVolumeInfo() != null)
								{
										book.setAuthor(new Author(item.getVolumeInfo().getAuthors()));
										book.setTitle(item.getVolumeInfo().getTitle());
										book.setImage(item.getVolumeInfo().getImageLinks().getThumbnail());
								}
								if (item.getSaleInfo() != null)
								{
										book.setPrice(item.getSaleInfo().getRetailPrice().getAmount());
										Double oldPrice = item.getSaleInfo().getListPrice().getAmount();
										book.setPriceOld(oldPrice.intValue());
								}
								list.add(book);
						}
				}
				return list;
		}

  public void createNewBook(String name, String description, String authorId, String price) {
			Author author = authorRepository.getById(Integer.valueOf(authorId));
			Book newBook = new Book();
			newBook.setAuthor(author);
			newBook.setTitle(name);
			newBook.setDescription(description);
			newBook.setSlug(String.valueOf(UUID.randomUUID()));
			newBook.setTag("en");
			newBook.setPrice(Double.valueOf(price));
			bookRepository.save(newBook);
  }
}
