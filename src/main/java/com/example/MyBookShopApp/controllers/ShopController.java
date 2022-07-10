package com.example.MyBookShopApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shop")
public class ShopController
{
		@GetMapping("/cart")
		public String cartPageMain()
		{//Model model){
				return "cart";
		}

		@GetMapping("/postponed")
		public String cartPagePostponed()
		{
				return "postponed";
		}

}
