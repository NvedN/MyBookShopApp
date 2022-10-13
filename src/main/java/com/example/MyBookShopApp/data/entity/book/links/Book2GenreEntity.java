package com.example.MyBookShopApp.data.entity.book.links;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.genre.GenreEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "book2genre")
public class Book2GenreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(columnDefinition = "INT NOT NULL")
//    private int bookId;

    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    @JsonIgnore
    private Book book;


    @ManyToOne( fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "genre_id", referencedColumnName = "id")
    @JsonIgnore
    private GenreEntity genre;
}
