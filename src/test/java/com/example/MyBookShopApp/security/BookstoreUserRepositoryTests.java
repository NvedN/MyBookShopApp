package com.example.MyBookShopApp.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookstoreUserRepositoryTests {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserRepositoryTests(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Test
    public void testAddNewUser(){
        BookstoreUser user = new BookstoreUser();
        user.setPassword("1234567890");
        user.setPhone("9031232323");
        user.setName("Tester");
        user.setEmail("testing@mailtest.org");

        assertNotNull(bookstoreUserRepository.save(user));
    }
}