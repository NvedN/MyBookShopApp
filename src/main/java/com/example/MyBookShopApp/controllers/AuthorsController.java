package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/authors")
public class AuthorsController
{

		private final AuthorService authorService;


		private static JdbcTemplate jdbcTemplate;

		@Autowired
		public AuthorsController(AuthorService authorService)
		{
				this.authorService = authorService;
		}

		@GetMapping("/main")
		public String genresPage(Model model) throws Exception
		{
				model.addAttribute("authorsData", AuthorService.sortAuthorsByFirstLetter(authorService.getAuthorsData()));
				return "/authors/index";
		}

		public HashSet<String> getListLetters(List<Author> authorList)
		{
				HashSet<String> returnLetters = new HashSet<>();
				for (Author author: authorList){
						String firstLetter = String.valueOf(author.getLast_name().charAt(0));
						returnLetters.add(firstLetter);
				}

				return returnLetters;
		}

		@GetMapping("/slug")
		public String genresSlugPage(){
				return "/authors/slug";
		}

}
