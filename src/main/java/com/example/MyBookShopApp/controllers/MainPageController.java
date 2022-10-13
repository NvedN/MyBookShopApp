package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.RecommendedBooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.entity.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.data.entity.book.links.Book2GenreEntity;
import com.example.MyBookShopApp.data.entity.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.repository.Book2AuthorRepository;
import com.example.MyBookShopApp.data.repository.Book2GenreRepository;
import com.example.MyBookShopApp.data.repository.Book2UserRepository;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import com.example.MyBookShopApp.service.BookService;
import java.util.HashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainPageController {

  private final BookService bookService;
  private final BookstoreUserRegister userRegister;
  private BookRepository bookRepository;
  private Book2AuthorRepository book2AuthorRepository;
  private Book2GenreRepository book2GenreRepository;
  private Book2UserRepository book2UserRepository;

  @Autowired
  public MainPageController(
      BookService bookService,
      BookstoreUserRegister userRegister,
      BookRepository bookRepository,
      Book2AuthorRepository book2AuthorRepository,
      Book2GenreRepository book2GenreRepository,
      Book2UserRepository book2UserRepository) {
    this.bookService = bookService;
    this.userRegister = userRegister;
    this.bookRepository = bookRepository;
    this.book2AuthorRepository = book2AuthorRepository;
    this.book2GenreRepository = book2GenreRepository;
    this.book2UserRepository = book2UserRepository;
  }

  @ModelAttribute("recommendedBooks")
  public List<Book> recommendedBooks() {
    return bookService.getPageOfRecommendedBooks(0, 6).getContent();
  }

  @ModelAttribute("newsBooks")
  public List<Book> newsBooks() {
    return bookService.getPageOfNewsBooks(0, 6).getContent();
  }

  @ModelAttribute("popularBooks")
  public List<Book> popularsBooks() {
    return bookService.getPageOfRecommendedBooks(0, 6).getContent();
  }

  @GetMapping("/")
  public String mainPage(Model model, SearchWordDto searchWordDto) {
    model.addAttribute("searchWordDto", searchWordDto);
    return "index";
  }

  @GetMapping("/books/recommended")
  @ResponseBody
  public RecommendedBooksPageDto getBooksPage(
      @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
    return new RecommendedBooksPageDto(
        bookService.getPageOfRecommendedBooks(offset, limit).getContent());
  }

  @GetMapping("/books/newsBooks")
  @ResponseBody
  public BooksPageDto getNewsBooksPage(
      @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
    return new BooksPageDto(bookService.getPageOfNewsBooks(offset, limit).getContent());
  }

  @GetMapping("/books/popularBooks")
  @ResponseBody
  public BooksPageDto getPopularBooksPage(
      @RequestParam("offset") Integer offset, @RequestParam("limit") Integer limit) {
    return new BooksPageDto(bookService.getPageOfNewsBooks(offset, limit).getContent());
  }

  @ModelAttribute("tags")
  public HashSet<String> mainPageTags() {
    return bookService.getBooksByTag();
  }

  @GetMapping("/tags")
  public String mainPageTags(
      @RequestParam(value = "tag", required = false) String tag, Model model) {
    model.addAttribute("tagName", tag);
    model.addAttribute("tagResults", bookService.getPageOfTags(tag, 0, 20).getContent());
    return "/tags/index";
  }

  @PostMapping("/adminDeleteBook")
  public String deleteBookByAdmin(
      @RequestParam(value = "slug", required = true) String slug,
      Model model,
      SearchWordDto searchWordDto) {
    Book bookToDelete = bookRepository.findBookBySlug(slug);
    List<Book2AuthorEntity> book2AuthorEntities = book2AuthorRepository.getAllByBook_Slug(slug);
    book2AuthorRepository.deleteAll(book2AuthorEntities);
    List<Book2GenreEntity> book2GenreEntities = book2GenreRepository.getAllByBook_Slug(slug);
    book2GenreRepository.deleteAll(book2GenreEntities);
    List<Book2UserEntity> book2UserEntities = book2UserRepository.getAllByBook_Slug(slug);
    book2UserRepository.deleteAll(book2UserEntities);
    bookRepository.delete(bookToDelete);
    model.addAttribute("searchWordDto", searchWordDto);
    return "redirect:/";
  }
}
