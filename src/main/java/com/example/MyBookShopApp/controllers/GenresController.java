package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.entity.genre.GenreEntity;
import com.example.MyBookShopApp.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/genres")
public class GenresController
{

		private final GenreService genreService;

		@Autowired
		public GenresController(GenreService genreService)
		{
				this.genreService = genreService;
		}

		@GetMapping("/")
		public String genresMainPage(Model model, SearchWordDto searchWordDto)
		{//Model model){
				model.addAttribute("searchWordDto", searchWordDto);
				return "genres/index";
		}

		@ModelAttribute("genresMap")
		public Map<String, List<GenreEntity>> genresMap()
		{
				return genreService.getGenreMap();
		}

		@GetMapping("/slug")
		public String genresSlugPage(SearchWordDto searchWordDto, Model model,@RequestParam(value = "genreName", required = false) String genreName)
		{//Model model){
				model.addAttribute("genreName",genreName);
				model.addAttribute("searchWordDto", searchWordDto);
				model.addAttribute("genreResults",genreService.getPageOfGenreResultBooks(genreName,0,20));
				return "/genres/slug";
		}
}
