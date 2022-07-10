package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BookService {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    public List<Book> getBooksData(){
//        return jdbcTemplate.query("SELECT * FROM books",new BeanPropertyRowMapper<>(Book.class));
//    }


    public List<Book> getBooksData() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rownum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(getAuthorByAuthorId(rs.getInt("author_id")));
            book.setTitle(rs.getString("title"));
            book.setPriceOld(rs.getInt("price_old"));
            book.setPrice(rs.getInt("price"));
            return book;
        });
        return new ArrayList<>(books);
    }

    private String getAuthorByAuthorId(int author_id) {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors WHERE authors.id=" + author_id,
            (ResultSet rs, int rowNum) -> {
                Author author = new Author();
                author.setId(rs.getInt("id"));
                author.setFirstName(rs.getString("first_name"));
                author.setFirstName(rs.getString("last_name"));
                return author;
            });
        return authors.get(0).toString();
    }
}
