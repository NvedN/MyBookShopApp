package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GenersController
{

		@GetMapping("/genres")
		public String genresMainPage(){//Model model){
				return "/index";
		}

//		@GetMapping("/slug")
//		public String genresSlugPage(){//Model model){
//				return "/genres/slug";
//		}

}
