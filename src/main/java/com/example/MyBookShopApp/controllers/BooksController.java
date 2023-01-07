package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.entity.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.entity.user.BookstoreUser;
import com.example.MyBookShopApp.data.repository.AuthorRepository;
import com.example.MyBookShopApp.data.repository.Book2UserRepository;
import com.example.MyBookShopApp.exceptions.UserAttributesException;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.BooksRatingAndPopularityService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
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

  private final BookstoreUserRegister userRegister;
  private final BookService bookService;
  private final BooksRatingAndPopularityService booksRatingAndPopularityService;
  private final Book2UserRepository book2UserRepository;
  private final AuthorRepository authorRepository;

  @Autowired
  public BooksController(
      BookService bookService,
      BooksRatingAndPopularityService booksRatingAndPopularityService,
      BookstoreUserRegister userRegister,
      Book2UserRepository book2UserRepository, AuthorRepository authorRepository) {
    this.bookService = bookService;
    this.booksRatingAndPopularityService = booksRatingAndPopularityService;
    this.userRegister = userRegister;
    this.book2UserRepository = book2UserRepository;
    this.authorRepository = authorRepository;
  }

  @GetMapping("/author")
  public String booksPageAuthor() { // Model model){
    return "/books/author";
  }

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


  @GetMapping("/news/page")
  @ResponseBody
  public BooksPageDto getNewsBooks(@RequestParam(value = "from") String fromDate,
      @RequestParam(value = "to") String toDate,
      @RequestParam(value = "offset") Integer offset,
      @RequestParam(value = "limit") Integer limit) {
    return new BooksPageDto(
        bookService.findBooksByPubDateBetween(fromDate, toDate, offset, limit).getContent());
  }

  @GetMapping("/news")
  public String booksPageNews(Model model,
      SearchWordDto searchWordDto) {
    model.addAttribute("newsResults", bookService.findTopByPubDate(0, 5));
    model.addAttribute("searchWordDto", searchWordDto);
    return "books/news";
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
    return bookService.getBookSlug(slug, model, searchWordDto, cartContents);
  }

  @PostMapping("/{slug}/img/save")
  public String saveNewBookImage(
      @RequestParam("file") MultipartFile file, @PathVariable("slug") String slug)
      throws IOException {

    return bookService.saveBookImage(file, slug);

  }

  @GetMapping("/download/{hash}")
  public ResponseEntity<ByteArrayResource> bookFile(@PathVariable("hash") String hash)
      throws IOException {
    return bookService.downloadBookFile(hash);
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
    return bookService.changeBookRating(slug, text, slugCookie);

  }


  @GetMapping("/newBook")
  public String addNewBookPage(SearchWordDto searchWordDto, Model model) {
    model.addAttribute("authorsList", authorRepository.findAll());
    model.addAttribute("searchWordDto", searchWordDto);
    return "books/newBook";
  }

  @PostMapping("/newBook")
  public String createNewBook(SearchWordDto searchWordDto, Model model,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "description", required = false) String description,
      @RequestParam(value = "author", required = false) String authorId,
      @RequestParam(value = "price", required = false) String price

  ) {
    bookService.createNewBook(name, description, authorId, price);
    model.addAttribute("authorsList", authorRepository.findAll());
    model.addAttribute("searchWordDto", searchWordDto);
    return "books/newBook";
  }

}
