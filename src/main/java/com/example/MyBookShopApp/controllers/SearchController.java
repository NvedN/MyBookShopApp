package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SearchController
{

		@GetMapping("/search")
		public String genresMainPage(){//Model model){
				return "/index";
		}
}
