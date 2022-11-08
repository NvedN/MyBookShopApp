package com.example.MyBookShopApp.data.repository;

import com.example.MyBookShopApp.data.entity.book.review.BookReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<BookReviewEntity,Integer>
{

		BookReviewEntity findTopByOrderByIdDesc();

		BookReviewEntity findTopByOrderByUserIdDesc();
}
