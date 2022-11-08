package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private JdbcTemplate jdbcTemplate;
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate,AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Map<String, List<Author>> getAuthorsMap(){
        List<Author> authots = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rownum)->{
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        });

        return authots.stream().collect(Collectors.groupingBy((Author a)->{return a.getLastName().substring(0,1);}));
    }


    public List<Book> getPageOfAuthorResultBooks(String lastName, Integer offset, Integer limit)
    {
        Pageable nextPage = PageRequest.of(offset, limit);
        return  jdbcTemplate.query("SELECT * FROM book INNER JOIN book2author b2a on book.id = b2a.book_id INNER JOIN authors a on b2a.author_id = a.id WHERE a.last_name = "+ "'" +  lastName + "'",
            new BeanPropertyRowMapper<>(Book.class));
        //				return bookRepository.findBooksByGenreName(genreName);
    }

		public Author getAuthorInfo(String authorLastName)
		{
        System.out.println("----getAuthorInfo = ");
        return authorRepository.getAuthorByLastNameContaining(authorLastName);
		}
}
