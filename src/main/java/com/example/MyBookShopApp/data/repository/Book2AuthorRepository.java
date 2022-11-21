package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.data.entity.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.entity.user.BookstoreUser;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Book2AuthorRepository extends JpaRepository<Book2AuthorEntity, Integer> {

  List<Book2AuthorEntity> getAllByBook_Slug(String book_slug);

}
