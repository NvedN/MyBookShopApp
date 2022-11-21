package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.BookSorted;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookSortedRepository extends JpaRepository<BookSorted,Integer> {


    Page<BookSorted> findAll(Pageable nextPage);
}
