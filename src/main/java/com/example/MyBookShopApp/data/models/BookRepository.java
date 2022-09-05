package com.example.MyBookShopApp.data.models;

import com.example.MyBookShopApp.data.Book;
import org.h2.pagestore.db.PageBtreeLeaf;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BookRepository extends JpaRepository<Book,Integer> {

    List<Book> findBooksByAuthor_FirstName(String name);

    @Query("from Book")
    List<Book> customFindAllBooks();

    //NEW BOOK REST REPOSITORY

    List<Book> findBooksByAuthorFirstNameContaining(String authorsFirstName);

    List<Book> findBooksByTitleContaining(String bookTitle);

    List<Book> findBooksByPriceOldBetween(Integer min, Integer max);

    List<Book> findBooksByPriceOldIs(Integer price);

    @Query("from Book where isBestseller=1")
    List<Book> getBestsellers();

    @Query(value = "SELECT * FROM book WHERE discount = (SELECT MAX(discount) FROM books",nativeQuery = true)
    List<Book> getBooksWithMaxDiscount();


    Page<Book> findBooksByPubDateBetween(LocalDateTime startDate,LocalDateTime endDate, Pageable nextPage);
    List<Book> findByPubDateBetween(LocalDateTime fromDate, LocalDateTime toDate);

    Page<Book> findBookByTitleContaining(String bookTitle, Pageable nextPage );

    List<Book> findBooksByPubDate(LocalDateTime date);

    Page<Book> findBooksByPubDate(LocalDateTime date, Pageable nextPage);

    Page<Book> findTopByOrderByPubDateDesc(Pageable nextPage);

    Page<Book> findAllByOrderByPubDateDesc(Pageable nextPage);

    Page<Book> findBookByTagContaining(String tag, Pageable nextPage );

    @Query(value = "SELECT * FROM book INNER JOIN book2genre b2g on book.id = b2g.book_id INNER JOIN genre g on b2g.genre_id = g.id WHERE name = :genreName;",nativeQuery = true)
    List<Book> findBooksByGenreName(String genreName);

    Book findBookBySlug(String slug);

    List<Book> findBooksBySlugIn(String[] slugs);

}