package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookRepository;
import com.example.MyBookShopApp.data.entity.genre.GenreEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GenreService
{

		private JdbcTemplate jdbcTemplate;
		private BookRepository bookRepository;

		@Autowired
		public GenreService(JdbcTemplate jdbcTemplate,BookRepository bookRepository )
		{
				this.bookRepository = bookRepository;
				this.jdbcTemplate = jdbcTemplate;
		}

		public Map<String, List<GenreEntity>> getGenreMap()
		{
				List<GenreEntity> genres = jdbcTemplate.query("SELECT * FROM genre",
						new BeanPropertyRowMapper<>(GenreEntity.class));
				Map<Integer, List<GenreEntity>> collected = genres.stream()
						.collect(Collectors.groupingBy((GenreEntity a) -> a.getParentId()));
				HashMap<String, List<GenreEntity>> output = new HashMap<>();
				for (Integer key : collected.keySet())
				{
						for (GenreEntity genre : genres)
						{
								if (key.equals(genre.getParentId()))
								{
										output.put(genre.getName(), collected.get(key));
								}
						}
				}
				return output;
		}



		public List<Book> getPageOfGenreResultBooks(String genreName, Integer offset, Integer limit)
		{
				Pageable nextPage = PageRequest.of(offset, limit);
				return  jdbcTemplate.query("SELECT * FROM book INNER JOIN book2genre b2g on book.id = b2g.book_id INNER JOIN genre g on b2g.genre_id = g.id WHERE name ="+ "'" +  genreName + "'",
						new BeanPropertyRowMapper<>(Book.class));
//				return bookRepository.findBooksByGenreName(genreName);
		}
}
