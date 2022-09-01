package com.example.MyBookShopApp.data.entity.book.review;

import com.example.MyBookShopApp.data.Book;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_like")
public class BookReviewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_review_id", referencedColumnName = "id")
    private BookReviewEntity bookReviewEntity;

    @Column(columnDefinition = "INT NOT NULL")
    private int userId;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private Integer value;

    public BookReviewLikeEntity(Integer nextId, Integer rating, LocalDateTime now, Integer nextUserId, BookReviewEntity bookReviewEntity)
    {
    }
    public  BookReviewLikeEntity(){

    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public BookReviewEntity getBookReviewEntity()
    {
        return bookReviewEntity;
    }

    public void setBookReviewEntity(BookReviewEntity bookReviewEntity)
    {
        this.bookReviewEntity = bookReviewEntity;
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

    public Integer getValue()
    {
        return value;
    }

    public void setValue(Integer value)
    {
        this.value = value;
    }

    @Override public String toString()
    {
        return "BookReviewLikeEntity{" +
            "id=" + id +
            ", bookReviewEntity=" + bookReviewEntity +
            ", userId=" + userId +
            ", time=" + time +
            ", value=" + value +
            '}';
    }
}
