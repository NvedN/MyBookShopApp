package com.example.MyBookShopApp.data.models;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author,Integer>
{

		Author getAuthorByLastNameContaining(String authorLastName);
}
