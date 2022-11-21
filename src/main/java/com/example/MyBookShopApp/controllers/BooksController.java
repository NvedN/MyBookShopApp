package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.entity.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.entity.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.entity.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.data.entity.user.BookstoreUser;
import com.example.MyBookShopApp.data.repository.Book2UserRepository;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.ReviewLikeRepository;
import com.example.MyBookShopApp.data.repository.ReviewRepository;
import com.example.MyBookShopApp.exceptions.UserAttributesException;
import com.example.MyBookShopApp.security.BookstoreUserDetails;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.BooksRatingAndPopularityService;
import com.example.MyBookShopApp.service.ResourceStorage;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/books")
public class BooksController {

  private final ResourceStorage storage;
  private final BookstoreUserRegister userRegister;
  private final BookService bookService;
  private final BooksRatingAndPopularityService booksRatingAndPopularityService;
  private final BookRepository bookRepository;
  private final ReviewRepository reviewRepository;
  private final ReviewLikeRepository reviewLikeRepository;
  private final Book2UserRepository book2UserRepository;

  @Autowired
  public BooksController(
      BookService bookService,
      BooksRatingAndPopularityService booksRatingAndPopularityService,
      BookRepository bookRepository,
      ResourceStorage storage,
      ReviewRepository reviewRepository,
      ReviewLikeRepository reviewLikeRepository,
      BookstoreUserRegister userRegister,
      Book2UserRepository book2UserRepository) {
    this.bookService = bookService;
    this.booksRatingAndPopularityService = booksRatingAndPopularityService;
    this.bookRepository = bookRepository;
    this.storage = storage;
    this.reviewRepository = reviewRepository;
    this.reviewLikeRepository = reviewLikeRepository;
    this.userRegister = userRegister;
    this.book2UserRepository = book2UserRepository;
  }

  @GetMapping("/author")
  public String booksPageAuthor() { // Model model){
    return "/books/author";
  }

  //  books/popular?offset=20&limit=20
  @GetMapping("/popular")
  public String booksPagePopular(Model model, SearchWordDto searchWordDto)
      throws UserAttributesException {
    model.addAttribute("popularBooks", booksRatingAndPopularityService.findPopularsBooks(0, 5));
    model.addAttribute("searchWordDto", searchWordDto);
    return "/books/popular";
  }

  @GetMapping("/popular/NextPage")
  @ResponseBody
  public BooksPageDto booksPagePopular(@RequestParam(value = "offset") Integer offset,
      @RequestParam(value = "limit") Integer limit)
      throws UserAttributesException {
    return new BooksPageDto(
        bookService.getPageOfRecommendedBooks(offset, limit).getContent());
  }

  @GetMapping("/news")
  public String booksPageNews(Model model,
      SearchWordDto searchWordDto) {
    model.addAttribute("newsResults", bookService.findTopByPubDate(0, 5));
    model.addAttribute("searchWordDto", searchWordDto);
    return "books/news";
  }

  @GetMapping("/news/page")
  @ResponseBody
  public BooksPageDto getNewsBooks(@RequestParam(value = "from", required = false) String fromDate,
      @RequestParam(value = "to", required = false) String toDate,
      @RequestParam(value = "offset", required = false) Integer offset,
      @RequestParam(value = "limit", required = false) Integer limit) {
    return new BooksPageDto(
        (bookService.findBooksByPubDateBetween(fromDate, toDate, offset, limit)));
  }


  @GetMapping("/recent")
  public String booksPageRecent(Model model, SearchWordDto searchWordDto)
      throws UserAttributesException {
    BookstoreUser userDetails = (BookstoreUser) userRegister.getCurrentUser();
    List<Book2UserEntity> book2UserEntities =
        book2UserRepository.getAllByBookstoreUserAndTime(userDetails, LocalDate.now());
    model.addAttribute("recentResults", book2UserEntities);
    model.addAttribute("searchWordDto", searchWordDto);
    return "books/recent";
  }

  @ModelAttribute("booksList")
  public List<Book> bookList() {
    return bookService.getBooksData();
  }


  @ModelAttribute("bookPageDto")
  public BooksPageDto getNextNewsPage() {

    return new BooksPageDto();
  }

  @GetMapping("/{slug}")
  public String bookPage(
      @PathVariable("slug") String slug,
      Model model,
      SearchWordDto searchWordDto,
      @CookieValue(value = "cartContents", required = false) String cartContents)
      throws UserAttributesException {
    BookstoreUserDetails userDetails = new BookstoreUserDetails((BookstoreUser) userRegister.getCurrentUser());
    Book book = bookRepository.findBookBySlug(slug);
    if (book != null) {
      Book2UserEntity book2UserEntity = new Book2UserEntity();
      book2UserEntity.setBookstoreUser(userDetails.getBookstoreUser());
      book2UserEntity.setBook(book);
      book2UserEntity.setTime(LocalDate.now());
      book2UserRepository.save(book2UserEntity);
      Collection<? extends GrantedAuthority> getAuthoritiesAdmin = userDetails.getAuthoritiesAdmin();
      boolean isAdmin = getAuthoritiesAdmin != null;
      model.addAttribute("admin", isAdmin);
      model.addAttribute("searchWordDto", searchWordDto);
      model.addAttribute("slugBook", book);
      model.addAttribute("reviewList", bookService.bookReviewEntityList(book));
    }
    return "books/slugmy";
  }

  @PostMapping("/{slug}/img/save")
  public String saveNewBookImage(
      @RequestParam("file") MultipartFile file, @PathVariable("slug") String slug)
      throws IOException {
    String savePath = storage.saveNewBookImage(file, slug);
    Book bookToUpdate = bookRepository.findBookBySlug(slug);
    bookToUpdate.setImage(savePath);
    bookRepository.save(bookToUpdate); // save new path in db here
    return ("redirect:/books/" + slug);
  }

  @GetMapping("/download/{hash}")
  public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash)
      throws IOException {
    Path path = storage.getBookFilePath(hash);
    Logger.getLogger(this.getClass().getSimpleName()).info("book file path: " + path);
    MediaType mediaType = storage.getBookFileMime(hash);
    Logger.getLogger(this.getClass().getSimpleName()).info("book file mime type: " + mediaType);
    byte[] data = storage.getBookFileByteArray(hash);
    Logger.getLogger(this.getClass().getSimpleName()).info("book file data len: " + data.length);
    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
        .contentType(mediaType)
        .contentLength(data.length)
        .body(new ByteArrayResource(data));
  }

  @PostMapping("/changeBookStatus/{slug}/{rating}")
  public void addRatingToCookie(
      @PathVariable("slug") String slug,
      @PathVariable("rating") String rating,
      HttpServletResponse response) {
    Cookie cookie = new Cookie("slugCookie", rating);
    cookie.setPath("/books");
    response.addCookie(cookie);
  }

  @PostMapping("/bookReview/{slug}/{text}")
  public String changeBookRating(
      @PathVariable("slug") String slug,
      @PathVariable("text") String text,
      @CookieValue(name = "slugCookie", required = false) String slugCookie,
      HttpServletResponse response)
      throws Exception {
    Book book = bookRepository.findBookBySlug(slug);
    BookReviewEntity previousReviewInDb = reviewRepository.findTopByOrderByIdDesc();
    Integer nextId = previousReviewInDb.getId();
    Integer nextUserId = previousReviewInDb.getUserId();
    //								nextId++;
    nextUserId++;
    Integer rating = 0;
    if (slugCookie != null && !slugCookie.equals("")) {
      rating = Integer.valueOf(slugCookie);
    }
    BookReviewEntity bookReviewEntity = new BookReviewEntity();
    bookReviewEntity.setBook(book);
    bookReviewEntity.setText(text);
    bookReviewEntity.setId(nextId);
    bookReviewEntity.setUserId(nextUserId);
    bookReviewEntity.setTime(LocalDateTime.now());
    BookReviewLikeEntity bookReviewLikeEntity = new BookReviewLikeEntity();
    bookReviewLikeEntity.setId(nextId);
    bookReviewLikeEntity.setValue(rating);
    bookReviewLikeEntity.setTime(LocalDateTime.now());
    bookReviewLikeEntity.setUserId(nextUserId);
    bookReviewLikeEntity.setBookReviewEntity(bookReviewEntity);
    reviewRepository.save(bookReviewEntity);
    reviewLikeRepository.save(bookReviewLikeEntity);
    return ("redirect:/books/" + slug);
  }
}
