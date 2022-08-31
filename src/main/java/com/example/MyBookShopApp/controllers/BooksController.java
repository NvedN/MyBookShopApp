package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.RecommendedBooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.entity.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.entity.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.data.models.BookRepository;
import com.example.MyBookShopApp.data.models.ReviewLikeRepository;
import com.example.MyBookShopApp.data.models.ReviewRepository;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.BooksRatingAndPopularityService;
import com.example.MyBookShopApp.service.ResourceStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Controller
@RequestMapping("/books")
public class BooksController
{

		private BookService bookService;

		private BooksRatingAndPopularityService booksRatingAndPopularityService;

		private BookRepository bookRepository;

		private final ResourceStorage storage;

		private ReviewRepository reviewRepository;

		private ReviewLikeRepository reviewLikeRepository;

		@Autowired
		public BooksController(BookService bookService,
				BooksRatingAndPopularityService booksRatingAndPopularityService,
				BookRepository bookRepository, ResourceStorage storage, ReviewRepository reviewRepository,ReviewLikeRepository reviewLikeRepository)
		{
				this.bookService = bookService;
				this.booksRatingAndPopularityService = booksRatingAndPopularityService;
				this.bookRepository = bookRepository;
				this.storage = storage;
				this.reviewRepository = reviewRepository;
				this.reviewLikeRepository = reviewLikeRepository;
		}

		@GetMapping("/author")
		public String booksPageAuthor()
		{//Model model){
				return "/books/author";
		}

		@GetMapping("/popular")
		public String booksPagePopular(Model model, SearchWordDto searchWordDto)
		{
				model.addAttribute("popularBooks",
						booksRatingAndPopularityService.findPopularsBooks(0, 5));
				model.addAttribute("searchWordDto", searchWordDto);
				return "/books/popular";
		}

		@GetMapping("/recent")
		public String booksPageRecent(@RequestParam(value = "from", required = false) String fromDate,
				@RequestParam(value = "to", required = false) String toDate,
				@RequestParam(value = "offset", required = false) Integer offset,
				@RequestParam(value = "limit", required = false) Integer limit,
				Model model, SearchWordDto searchWordDto)
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
				model.addAttribute("searchWordDto", searchWordDto);
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

		@GetMapping("/{slug}")
		public String bookPage(@PathVariable("slug") String slug, Model model, SearchWordDto searchWordDto,
				@CookieValue(value = "cartContents", required = false) String cartContents)
		{
				Book book = bookRepository.findBookBySlug(slug);
				if (book != null)
				{
						System.out.println("-----book.bookRating(); = "  + book.bookRating());
						model.addAttribute("searchWordDto", searchWordDto);
						model.addAttribute("slugBook", book);
						model.addAttribute("reviewList", bookService.bookReviewEntityList(book));

//						model.addAttribute("test", bookService.)
				}
				return "books/slugmy";
		}

		@PostMapping("/{slug}/img/save")
		public String saveNewBookImage(@RequestParam("file") MultipartFile file, @PathVariable("slug") String slug)
				throws IOException
		{
				String savePath = storage.saveNewBookImage(file, slug);
				Book bookToUpdate = bookRepository.findBookBySlug(slug);
				bookToUpdate.setImage(savePath);
				bookRepository.save(bookToUpdate); //save new path in db here
				return ("redirect:/books/" + slug);
		}

		@GetMapping("/download/{hash}")
		public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash) throws IOException
		{
				Path path = storage.getBookFilePath(hash);
				Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);
				MediaType mediaType = storage.getBookFileMime(hash);
				Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);
				byte[] data = storage.getBookFileByteArray(hash);
				Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
						.contentType(mediaType)
						.contentLength(data.length)
						.body(new ByteArrayResource(data));
		}

//		/books/changeBookStatus/' + + $this.data('bookid')
		@PostMapping("/changeBookStatus/{slug}/{rating}")
		public void addRatingToCookie(@PathVariable("slug") String slug, @PathVariable("rating") String rating,
				HttpServletResponse response){
				Cookie cookie = new Cookie("slugCookie", rating);
				cookie.setPath("/books");
				response.addCookie(cookie);

		}

		@PostMapping("/bookReview/{slug}/{text}")
		public String changeBookRating(@PathVariable("slug") String slug, @PathVariable("text") String text,@CookieValue(name =
				"slugCookie", required = false) String slugCookie, HttpServletResponse response) throws Exception
		{
				System.out.println("----slug = " + slug);
				System.out.println("-------text = " + text);
				Book book = bookRepository.findBookBySlug(slug);
				BookReviewEntity previousReviewInDb =  reviewRepository.findTopByOrderByIdDesc();
				System.out.println("---previous review = " + previousReviewInDb);

				Integer nextId = previousReviewInDb.getId();
				Integer nextUserId = previousReviewInDb.getUserId();
				nextId++;
				nextUserId++;
				Integer rating = 0;
				System.out.println("-----slugCookie = " + slugCookie);
				if (slugCookie != null || !slugCookie.equals(""))
				{
						ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(slugCookie.split("/")));
						System.out.println("------cookie Books = " + cookieBooks);
//						rating = Integer.parseInt(cookieBooks.get(slugCookie));
						rating = Integer.valueOf(slugCookie);
				}
				System.out.println("----------raitng = " + rating);
				BookReviewEntity bookReviewEntity  = new BookReviewEntity();
//				reviewRepository.save(bookReviewEntity);
				bookReviewEntity.setBook(book);
				bookReviewEntity.setText(text);

				bookReviewEntity.setId(nextId);
				bookReviewEntity.setUserId(nextUserId);
				bookReviewEntity.setTime(LocalDateTime.now());
				System.out.println("----------bookReviewEntity = " + bookReviewEntity);

				BookReviewLikeEntity bookReviewLikeEntity = new BookReviewLikeEntity();
				bookReviewLikeEntity.setId(nextId);
				bookReviewLikeEntity.setValue(rating);
				bookReviewLikeEntity.setTime(LocalDateTime.now());
				bookReviewLikeEntity.setUserId(nextUserId);
				bookReviewLikeEntity.setBookReviewEntity(bookReviewEntity);

				System.out.println("-----bookReviewLikeEntity = " + bookReviewLikeEntity);

				try
				{
						reviewRepository.save(bookReviewEntity);
						reviewLikeRepository.save(bookReviewLikeEntity);
				}catch (Exception e){
						System.out.println("-------Start Exception?");
						e.printStackTrace(System.out);
				}

				return ("redirect:/books/" + slug);
		}
}
