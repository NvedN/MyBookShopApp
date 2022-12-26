package com.example.MyBookShopApp.data.entity.book.review;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.file.BookFileEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_review")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne(optional = false)
    @JsonIgnore
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    private int userId;

    private LocalDateTime time;

    private String text;

    public BookReviewEntity(Integer nextId, Book book, String text, Integer nextUserId, LocalDateTime now)
    {
    }
    public BookReviewEntity(){

    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @OneToMany(mappedBy = "bookReviewEntity", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
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
