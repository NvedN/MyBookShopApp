package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
//@RequestMapping("/tags")
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


//		@GetMapping("/search/page/{tagName}")
//		@ResponseBody
//		public BooksPageDto getNextTagPage(@RequestParam("offset") Integer offset,
//				@RequestParam("limit") Integer limit,
//				@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
//				System.out.println("------------page TAg ? ");
//				return new BooksPageDto(bookService.getPageOfTags(searchWordDto.getExample(), offset, limit).getContent());
//		}
}
