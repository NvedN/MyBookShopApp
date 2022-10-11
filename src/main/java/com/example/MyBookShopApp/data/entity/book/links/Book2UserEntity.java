package com.example.MyBookShopApp.data.entity.book.links;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.user.BookstoreUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book2user")
public class Book2UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDate time;


    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @JsonIgnore
    private Book book;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    @JsonIgnore
    private BookstoreUser bookstoreUser;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

//    public int getTypeId() {
//        return typeId;
//    }
//
//    public void setTypeId(int typeId) {
//        this.typeId = typeId;
//    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookstoreUser getBookstoreUser() {
        return bookstoreUser;
    }

    public void setBookstoreUser(BookstoreUser bookstoreUser) {
        this.bookstoreUser = bookstoreUser;
    }

    @Override
    public String toString() {
        return "Book2UserEntity{" +
            "id=" + id +
            ", time=" + time +
            ", book=" + book +
            ", bookstoreUser=" + bookstoreUser +
            '}';
    }
}
