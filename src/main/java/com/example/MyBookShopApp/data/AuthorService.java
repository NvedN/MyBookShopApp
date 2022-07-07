package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthorService
{

		private  static JdbcTemplate jdbcTemplate;

		@Autowired
		public AuthorService(JdbcTemplate jdbcTemplate) {
				this.jdbcTemplate = jdbcTemplate;
		}

		public List<Author> getAuthorsData()
		{
				return jdbcTemplate.query("SELECT * FROM authors",new BeanPropertyRowMapper<>(Author.class));

		}

}
