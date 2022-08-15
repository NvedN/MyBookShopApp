package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagControllers
{

		private final BookService bookService;

		@Autowired
		public TagControllers(BookService bookService)
		{
				this.bookService = bookService;
		}


//		@GetMapping("/")
//		public String mainPage(@RequestParam(value = "tag",required = false) String tag ,Model model)
//		{
//				System.out.println("-----startmainPage");
//				System.out.println("--------tag = " + tag);
//				model.addAttribute("tags", bookService.getBooksByTag());
//				return "/tags/index";
//		}
}
