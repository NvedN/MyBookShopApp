package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.MyBookShopApp.data.BookService;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController
{
		private final BookService bookService;

		@Autowired
		public SearchController(BookService bookService)
		{
				this.bookService = bookService;
		}


		@ModelAttribute("searchWordDto")
		public SearchWordDto searchWordDto(){
				return new SearchWordDto();
		}

		@ModelAttribute("searchResults")
		public List<Book> searchResults(){
				return new ArrayList<>();
		}

		@GetMapping(value = {"/search" , "/search/{searchWord}"})
		public String getSearchResult(@PathVariable(value = "searchWord", required = false)
				SearchWordDto searchWordDto, Model model){//Model model){

				model.addAttribute("searchWordDto", searchWordDto);
				model.addAttribute("searchResults",
						bookService.getPageOfSearchResultBooks(searchWordDto.getExample(),0,20).getContent());
				return "search/index";
		}
}
