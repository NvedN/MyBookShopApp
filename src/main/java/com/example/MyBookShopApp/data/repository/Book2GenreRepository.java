package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.book.links.Book2AuthorEntity;
import com.example.MyBookShopApp.data.entity.book.links.Book2GenreEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Book2GenreRepository extends JpaRepository<Book2GenreEntity, Integer> {

  List<Book2GenreEntity> getAllByBook_Slug(String book_slug);

}
