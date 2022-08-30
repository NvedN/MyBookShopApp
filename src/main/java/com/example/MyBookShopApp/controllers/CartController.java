package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.models.BookRepository;
import com.example.MyBookShopApp.data.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

@Controller
@RequestMapping("/shop")
public class CartController
{

		@ModelAttribute(name = "bookCart")
		public List<Book> bookCart()
		{
				return new ArrayList<>();
		}

		private final BookRepository bookRepository;

		@Autowired
		public CartController(BookRepository bookRepository)
		{
				this.bookRepository = bookRepository;
		}

		@GetMapping("/cart")
		public String handleCartRequest(@CookieValue(value = "cartContents", required = false) String cartContents,
				Model model, SearchWordDto searchWordDto)
		{
				getContents(cartContents, model, searchWordDto);
				return "cart";
		}

		@GetMapping("/postponed")
		public String cartPagePostponed(
				@CookieValue(value = "postponedContents", required = false) String postponedContents,
				Model model, SearchWordDto searchWordDto)
		{
				getContents(postponedContents, model, searchWordDto);
				return "postponed";
		}

		private void getContents(String contents,
				Model model, SearchWordDto searchWordDto)
		{
				model.addAttribute("searchWordDto", searchWordDto);
				System.out.println("-------------NVN-----------cartContents = " + contents);
				if (contents == null || contents.equals(""))
				{
						model.addAttribute("isCartEmpty", true);
				}
				else
				{
						model.addAttribute("isCartEmpty", false);
						contents = contents.startsWith("/") ? contents.substring(1) : contents;
						contents = contents.endsWith("/") ? contents.substring(0, contents.length() - 1) :
								contents;
						String[] cookieSlugs = contents.split("/");
						List<Book> booksFromCookieSlugs = bookRepository.findBooksBySlugIn(cookieSlugs);
						model.addAttribute("bookCart", booksFromCookieSlugs);
				}
		}

		@PostMapping("/changeBookStatus/cart/remove/{slug}")
		public String handleRemoveBookFromCartRequest(@PathVariable("slug") String slug,
				@CookieValue(value = "cartContents", required = false) String cartContents, HttpServletResponse response, Model model) {
				if (cartContents != null && !cartContents.equals("")) {
						ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(cartContents.split("/")));
						cookieBooks.remove(slug);
						Cookie cookie = new Cookie("cartContents", String.join("/", cookieBooks));
						cookie.setPath("/shop");
						response.addCookie(cookie);
						model.addAttribute("isCartEmpty", false);
				} else {
						model.addAttribute("isCartEmpty", true);
				}

				return "redirect:/shop/cart";
		}


		@PostMapping("/changeBookStatus/postponed/remove/{slug}")
//									changeBookStatus/posptponed/remove/book-ilp-658
		public String handleRemoveBookFromPostponedRequest(@PathVariable("slug") String slug, @CookieValue(name =
				"postponedContents", required = false) String postponedContents, HttpServletResponse response, Model model)
		{
				System.out.println("---------delete postponed  content = " + postponedContents);

				if (postponedContents != null && !postponedContents.equals(""))
				{
						ArrayList<String> cookieBooks = new ArrayList<>(Arrays.asList(postponedContents.split("/")));
						cookieBooks.remove(slug);
						Cookie cookie = new Cookie("postponedContents", String.join("/", cookieBooks));
						cookie.setPath("/shop");
						response.addCookie(cookie);
						model.addAttribute("isCartEmpty", false);
				}
				else
				{
						model.addAttribute("isCartEmpty", true);
				}
				return "redirect:/shop/cart";
		}

		@PostMapping("/changeBookStatus/cart/{slug}")
		public String handleChangeBookStatus(@PathVariable("slug") String slug, @CookieValue(name = "cartContents",
				required = false) String cartContents, HttpServletResponse response, Model model) {

				if (cartContents == null || cartContents.equals("")) {
						Cookie cookie = new Cookie("cartContents", slug);
						cookie.setPath("/shop");
						response.addCookie(cookie);
						model.addAttribute("isCartEmpty", false);
				} else if (!cartContents.contains(slug)) {
						StringJoiner stringJoiner = new StringJoiner("/");
						stringJoiner.add(cartContents).add(slug);
						Cookie cookie = new Cookie("cartContents", stringJoiner.toString());
						cookie.setPath("/shop");
						response.addCookie(cookie);
						model.addAttribute("isCartEmpty", false);
				}

				return ("redirect:/books/" + slug);
		}

		@PostMapping("/changeBookStatus/postponed/{slug}")
		public String handleChangePostponedBookStatus(@PathVariable("slug") String slug,
				@CookieValue(name = "postponedContents",
						required = false) String postponedContents, HttpServletResponse response, Model model)
		{
				System.out.println("-------POSTPONED !!!!");
				if (postponedContents == null || postponedContents.equals(""))
				{
						Cookie cookie = new Cookie("postponedContents", slug);
						cookie.setPath("/shop");
//						cookie.setHttpOnly(true);
						System.out.println("-----------cookie = " + cookie);
						response.addCookie(cookie);
						model.addAttribute("isCartEmpty", false);
				}
				else if (!postponedContents.contains(slug))
				{
						StringJoiner stringJoiner = new StringJoiner("/");
						stringJoiner.add(postponedContents).add(slug);
						Cookie cookie = new Cookie("postponedContents", stringJoiner.toString());
						cookie.setPath("/shop");
//						cookie.setHttpOnly(true);
						response.addCookie(cookie);
						model.addAttribute("isCartEmpty", false);
				}
				return ("redirect:/books/" + slug);
		}
}
