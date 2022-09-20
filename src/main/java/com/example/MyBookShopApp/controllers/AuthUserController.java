package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.SmsCode;
import com.example.MyBookShopApp.exceptions.UserAttributesException;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import com.example.MyBookShopApp.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.security.RegistrationForm;
import com.example.MyBookShopApp.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
public class AuthUserController
{

		private final BookstoreUserRegister userRegister;

		private final SmsService smsService;

		private final JavaMailSender javaMailSender;

		@Autowired
		public AuthUserController(BookstoreUserRegister userRegister,SmsService smsService,JavaMailSender javaMailSender)
		{

        this.userRegister = userRegister;
        this.smsService = smsService;
        this.javaMailSender = javaMailSender;

		}

		@GetMapping("/signin")
		public String handleSignin()
		{
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
		public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload) {
				ContactConfirmationResponse response = new ContactConfirmationResponse();
				response.setResult("true");

				if(payload.getContact().contains("@")){
						return response;
				}else{
						String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
						smsService.saveNewCode(new SmsCode(smsCodeString,60)); //expires in 1 min.
						return response;
				}
		}

		@PostMapping("/requestEmailConfirmation")
		@ResponseBody
		public ContactConfirmationResponse handleRequestEmailConfirmation(@RequestBody ContactConfirmationPayload payload)
		{
				ContactConfirmationResponse response = new ContactConfirmationResponse();
				SimpleMailMessage message = new SimpleMailMessage();
				message.setFrom("mybook.shopapp@bk.ru");
				message.setTo(payload.getContact());
				SmsCode smsCode = new SmsCode(smsService.generateCode(), 300); //5 minutes
				smsService.saveNewCode(smsCode);
				message.setSubject("Bookstore email verification!");
				message.setText("Verification code is: " + smsCode.getCode());
				System.out.println("------javaMailSender = " + javaMailSender);
				javaMailSender.send(message);
				response.setResult("true");
				return response;
		}

		@PostMapping("/approveContact")
		@ResponseBody
		public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload)
		{
				ContactConfirmationResponse response = new ContactConfirmationResponse();
				response.setResult("true");
				return response;
		}

		@PostMapping("/reg")
		public String handleUserRegistration(RegistrationForm registrationForm, Model model)
		{
				userRegister.registerNewUser(registrationForm);
				model.addAttribute("regOk", true);
				return "signin";
		}

		@PostMapping("/login")
		@ResponseBody
		public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
				HttpServletResponse httpServletResponse)
		{
				ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
				Cookie cookie = new Cookie("token", loginResponse.getResult());
				httpServletResponse.addCookie(cookie);
				return loginResponse;
		}

		@GetMapping("/my")
		public String handleMy(SearchWordDto searchWordDto, Model model)
		{
				model.addAttribute("searchWordDto", searchWordDto);
				return "my";
		}

		@GetMapping("/profile")
		public String handleProfile(Model model, SearchWordDto searchWordDto) throws UserAttributesException
		{
				model.addAttribute("curUsr", userRegister.getCurrentUser());
				model.addAttribute("searchWordDto", searchWordDto);
				return "profile";
		}
		//    @GetMapping("/logout")
		//    public String handleLogout(HttpServletRequest request) {
		//        HttpSession session = request.getSession();
		//        SecurityContextHolder.clearContext();
		//        if (session != null) {
		//            session.invalidate();
		//        }
		//
		//        for (Cookie cookie : request.getCookies()) {
		//            cookie.setMaxAge(0);
		//        }
		//
		//        return "redirect:/";
		//    }
}
