package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.entity.user.BookstoreUser;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Integer> {

  List<Book2UserEntity> getAllByBookstoreUser(BookstoreUser bookstoreUser);

  List<Book2UserEntity> getAllByBookstoreUserAndBook(BookstoreUser bookstoreUser, Book book);

  List<Book2UserEntity> getAllByBookstoreUserAndTime(BookstoreUser bookstoreUser, LocalDate time);

  List<Book2UserEntity> getAllByBook_Slug(String book_slug);

}
