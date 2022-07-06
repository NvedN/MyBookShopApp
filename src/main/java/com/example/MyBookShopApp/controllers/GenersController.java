package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/genres")
public class GenersController
{

		@GetMapping("/main")
		public String genresPage(){//Model model){
				System.out.println("------getMapping genres");
				//        model.addAttribute("bookData",bookService.getBooksData());
				return "/genres/index";
		}

}
