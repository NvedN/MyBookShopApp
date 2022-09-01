package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.SearchWordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthUserController
{

		private final BookstoreUserRegister userRegister;

		@Autowired
		public AuthUserController(BookstoreUserRegister userRegister)
		{
				this.userRegister = userRegister;
		}

		@GetMapping("/signin")
		public String handleSignin(Model model, SearchWordDto searchWordDto)
		{
				model.addAttribute("searchWordDto", searchWordDto);
        return "signin";
		}

		@GetMapping("/signup")
		public String handleSignUp(Model model, SearchWordDto searchWordDto)
		{
				model.addAttribute("regForm", new RegistrationForm());
				model.addAttribute("searchWordDto", searchWordDto);

				return "signup";
		}

		@PostMapping("/requestContactConfirmation")
		@ResponseBody
		public ContactConfirmationResponse handleRequestContactConfirmation(
				@RequestBody ContactConfirmationPayload contactConfirmationPayload)
		{
				ContactConfirmationResponse response = new ContactConfirmationResponse();
				response.setResult(true);
				return response;
		}

		@PostMapping("/approveContact")
		@ResponseBody
		public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload)
		{
				ContactConfirmationResponse response = new ContactConfirmationResponse();
				response.setResult(true);
				return response;
		}

		@PostMapping("/reg")
		public String handleUserRegistration(RegistrationForm registrationForm, Model model, SearchWordDto searchWordDto)
		{
				userRegister.registerNewUser(registrationForm);
				model.addAttribute("regOk", true);
				model.addAttribute("searchWordDto", searchWordDto);

				return "signin";
		}

		@PostMapping("/login")
		@ResponseBody
		public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload)
		{
				return userRegister.login(payload);
		}

		@GetMapping("/my")
		public String handleMy(Model model, SearchWordDto searchWordDto)
		{
				model.addAttribute("searchWordDto", searchWordDto);

				return "my";
		}

		@GetMapping("/profile")
		public String handleProfile(Model model, SearchWordDto searchWordDto)
		{
				model.addAttribute("searchWordDto", searchWordDto);
				model.addAttribute("curUsr", userRegister.getCurrentUser());
				return "profile";
		}

		@GetMapping("/logout")
		public String handleLogout(HttpServletRequest request, SearchWordDto searchWordDto, Model model)
		{
				model.addAttribute("searchWordDto", searchWordDto);

				HttpSession session = request.getSession();
				SecurityContextHolder.clearContext();
				if (session != null)
				{
						session.invalidate();
				}
				for (Cookie cookie : request.getCookies())
				{
						cookie.setMaxAge(0);
				}
				return "redirect:/";
		}
}
