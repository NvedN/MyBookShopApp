package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
		public String genresPage(Model model){
				model.addAttribute("authorsData", authorService.getAuthorsData());
				return "/authors/index";
		}

		@GetMapping("/slug")
		public String genresSlugPage(){
				return "/authors/slug";
		}

}
