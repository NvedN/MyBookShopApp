package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.models.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository)
    {
        this.authorRepository = authorRepository;
    }

    public Map<String, List<Author>> getAuthorsMap(){
        List<Author> authors = authorRepository.findAll();
        return authors.stream().collect(Collectors.groupingBy((Author a)-> a.getLastName().substring(0,1)));
    }
}
