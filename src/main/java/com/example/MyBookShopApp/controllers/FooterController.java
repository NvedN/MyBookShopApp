package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class FooterController
{

		@GetMapping("/signing")
		public String booksPageSigning()
		{//Model model){
				return "signin";
		}

		@GetMapping("/document")
		public String footInfoPageDocument()
		{//Model model){
				return "/documents/index";
		}

		@GetMapping("/about")
		public String footInfoPageAbout()
		{//Model model){
				return "about";
		}

		@GetMapping("/faq")
		public String footInfoPageFaq()
		{//Model model){
				return "faq";
		}

		@GetMapping("/contacts")
		public String footInfoPageContacts()
		{//Model model){
				return "contacts";
		}
}
