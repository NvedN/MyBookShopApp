package com.example.MyBookShopApp.data.models;

import com.example.MyBookShopApp.data.entity.book.review.BookReviewLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<BookReviewLikeEntity,Integer>
{

}
