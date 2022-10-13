package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.AuthorPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.service.AuthorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Map;

@Controller
@Api(description = "authors data")
@RequestMapping("/authors")
public class AuthorsController {

    private final AuthorService authorService;

    @Autowired
    public AuthorsController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ModelAttribute("authorsMap")
    public Map<String,List<Author>> authorsMap(){
        return authorService.getAuthorsMap();
    }

    @GetMapping("")
    public String authorsPage(Model model, SearchWordDto searchWordDto){
        model.addAttribute("searchWordDto", searchWordDto);
        return "/authors/index";
    }

    @ApiOperation("method to get map of authors")
    @GetMapping("/api/authors")
    @ResponseBody
    public Map<String, List<Author>> authors(){
        return authorService.getAuthorsMap();
    }


    @ModelAttribute("authorPageDto")
    public AuthorPageDto authorPageDto(){
        return new AuthorPageDto();
    }

    @GetMapping("/slug")
    public String genresSlugPage(SearchWordDto searchWordDto, Model model,@RequestParam(value = "authorLastName", required = false) String authorLastName,
        AuthorPageDto authorPageDto)
    {//Model model){
        model.addAttribute("authorPageObject",authorService.getAuthorInfo(authorLastName));
        model.addAttribute("authorLastName",authorLastName);
        model.addAttribute("searchWordDto", searchWordDto);
        model.addAttribute("authorResults",authorService.getPageOfAuthorResultBooks(authorLastName,0,20));
        return "/authors/slug";
    }
}
