package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.BookSorted;
import com.example.MyBookShopApp.data.repository.Book2UserRepository;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.BookSortedRepository;
import com.example.MyBookShopApp.exceptions.UserAttributesException;
import com.example.MyBookShopApp.security.BookstoreUserRegister;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BooksRatingAndPopularityService {

    private BookRepository bookRepository;

    private BookSortedRepository bookSortedRepository;

    private Book2UserRepository book2UserRepository;

    private final BookstoreUserRegister userRegister;

    @Autowired
    public BooksRatingAndPopularityService(
            BookRepository bookRepository,
            Book2UserRepository book2UserRepository,
            BookstoreUserRegister userRegister, BookSortedRepository bookSortedRepository) {
        this.bookRepository = bookRepository;
        this.book2UserRepository = book2UserRepository;
        this.userRegister = userRegister;
        this.bookSortedRepository = bookSortedRepository;
    }

    public List<Book> findPopularsBooks(Integer offset, Integer limit)
            throws UserAttributesException {
        Pageable nextPage = PageRequest.of(offset, limit);
        List<BookSorted> bookSortedList = bookSortedRepository.findAll(nextPage).getContent();

        ArrayList<Book> allBooks = new ArrayList<>();

        for (BookSorted bookSorted : bookSortedList) {
            allBooks.add(bookSorted.getBook());
            System.out.println(bookSorted.getBook());
        }
        return allBooks;
    }

    public Page<Book> findPopularsBooksNextPage(Integer offset, Integer limit)
            throws UserAttributesException {
        Pageable nextPage = PageRequest.of(offset, limit);
        Page<BookSorted> bookSortedList = bookSortedRepository.findAll(nextPage);

        ArrayList<Book> books = new ArrayList<>();

        for (BookSorted bookSorted : bookSortedList) {
            books.add(bookSorted.getBook());
            System.out.println(bookSorted.getBook());
        }
        Page<Book> allBooks = new PageImpl<>(books);

        return allBooks ;
    }
}
