package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.*;
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
import java.nio.file.Path;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.List;




@Controller
@RequestMapping("/books")
public class BooksController
{

		private BookService bookService;

		private BooksRatingAndPopularityService booksRatingAndPopularityService;

		private BookRepository bookRepository;

		private final ResourceStorage storage;

		@Autowired
		public BooksController(BookService bookService,
				BooksRatingAndPopularityService booksRatingAndPopularityService,
				BookRepository bookRepository, ResourceStorage storage)
		{
				this.bookService = bookService;
				this.booksRatingAndPopularityService = booksRatingAndPopularityService;
				this.bookRepository = bookRepository;
				this.storage = storage;
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
		public String bookPage(@PathVariable("slug") String slug, Model model, SearchWordDto searchWordDto)
		{
				Book book = bookRepository.findBookBySlug(slug);
				if (book != null)
				{
						model.addAttribute("searchWordDto", searchWordDto);
						model.addAttribute("slugBook", book);
				}
				return "books/slug";
		}

		@PostMapping("/{slug}/img/save")
		public String saveNewBookImage(@RequestParam("file")MultipartFile file,@PathVariable("slug")String slug) throws IOException {
				String savePath = storage.saveNewBookImage(file,slug);
				Book bookToUpdate = bookRepository.findBookBySlug(slug);
				bookToUpdate.setImage(savePath);
				bookRepository.save(bookToUpdate); //save new path in db here
				return ("redirect:/books/" + slug);
		}

		@GetMapping("/download/{hash}")
		public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash")String hash) throws IOException{
				Path path = storage.getBookFilePath(hash);
				Logger.getLogger(this.getClass().getSimpleName()).info("book file path: "+path);

				MediaType mediaType = storage.getBookFileMime(hash);
				Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: "+mediaType);

				byte[] data = storage.getBookFileByteArray(hash);
				Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: "+data.length);

				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename="+path.getFileName().toString())
						.contentType(mediaType)
						.contentLength(data.length)
						.body(new ByteArrayResource(data));
		}
}
