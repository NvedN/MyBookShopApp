package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
public class GenersController
{

		@GetMapping("/main")
		public String genresMainPage(){//Model model){
				return "/genres/index";
		}

		@GetMapping("/slug")
		public String genresSlugPage(){//Model model){
				return "/genres/slug";
		}

}
