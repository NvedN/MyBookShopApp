package com.example.MyBookShopApp.data.entity.book.review;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.file.BookFileEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_review")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    private int userId;

    private LocalDateTime time;

    private String text;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @OneToMany(mappedBy = "bookReviewEntity")
    private List<BookReviewLikeEntity> bookReviewLikeEntities = new ArrayList<>();

    public List<BookReviewLikeEntity> getBookReviewLikeEntities()
    {
        return bookReviewLikeEntities;
    }

    public void setBookReviewLikeEntities(
        List<BookReviewLikeEntity> bookReviewLikeEntities)
    {
        this.bookReviewLikeEntities = bookReviewLikeEntities;
    }
    //    public List<BookReviewLikeEntity> getBookFileEntityList()
//    {
//        return bookReviewLikeEntities;
//    }




    //    @OneToOne(mappedBy = "book_review_like")
//    private BookReviewLikeEntity bookReviewLikeEntities ;
//
//    public List<BookReviewLikeEntity> getBookReviewLikeEntities()
//    {
//        return bookReviewLikeEntities;
//    }
//
//    public void setBookReviewLikeEntities(
//        List<BookReviewLikeEntity> bookReviewLikeEntities)
//    {
//        this.bookReviewLikeEntities = bookReviewLikeEntities;
//    }

    public Book getBook()
    {
        return book;
    }

    public void setBook(Book book)
    {
        this.book = book;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override public String toString()
    {
        return "BookReviewEntity{" +
            "id=" + id +
            ", book=" + book +
            ", userId=" + userId +
            ", time=" + time +
            ", text='" + text + '\'' +
            '}';
    }
}
