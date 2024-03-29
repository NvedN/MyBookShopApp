package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.exceptions.EmptySearchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.MyBookShopApp.service.BookService;

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
				SearchWordDto searchWordDto, Model model) throws EmptySearchException
		{//Model model){

				if (searchWordDto != null)
				{
						model.addAttribute("searchWordDto", searchWordDto);
						model.addAttribute("searchResults",
								bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), 0, 5));
						return "search/index";
				}else{
						throw new EmptySearchException("Поиск по null не возможен");
				}
		}

		@GetMapping("/search/page/{searchWord}")
		@ResponseBody
		public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
				@RequestParam("limit") Integer limit,
				@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
				return new BooksPageDto(bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), offset, limit));
		}
}
