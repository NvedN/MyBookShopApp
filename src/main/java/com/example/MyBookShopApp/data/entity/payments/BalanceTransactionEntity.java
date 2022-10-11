package com.example.MyBookShopApp.data.entity.payments;

import com.example.MyBookShopApp.data.entity.user.BookstoreUser;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "balance_transaction")
public class BalanceTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;



    @ManyToOne
    @JoinColumn(name = "bookstoreUser_id", referencedColumnName = "id")
    @JsonIgnore
    private BookstoreUser bookstoreUser;


    private LocalDate time;

    private int value;

    private int bookId;

    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BookstoreUser getBookstoreUser()
    {
        return bookstoreUser;
    }

    public void setBookstoreUser(BookstoreUser bookstoreUser)
    {
        this.bookstoreUser = bookstoreUser;
    }

    public LocalDate getTime() {
        return time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    @Override public String toString()
//    {
//        return "BalanceTransactionEntity{" +
//            "id=" + id +
//            ", bookstoreUser=" + bookstoreUser +
//            ", time=" + time +
//            ", value=" + value +
//            ", bookId=" + bookId +
//            ", description='" + description + '\'' +
//            '}';
//    }
}
