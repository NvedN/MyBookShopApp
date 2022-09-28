package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.SmsCode;
import com.example.MyBookShopApp.data.entity.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.data.entity.payments.BalanceTransactionRepository;
import com.example.MyBookShopApp.exceptions.UserAttributesException;
import com.example.MyBookShopApp.security.*;
import com.example.MyBookShopApp.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class AuthUserController
{

		private final BookstoreUserRegister userRegister;

		private final SmsService smsService;

		private final JavaMailSender javaMailSender;

		private final PasswordEncoder passwordEncoder;

		private final BookstoreUserRepository bookstoreUserRepository;

		private final BalanceTransactionRepository balanceTransactionRepository;

		@Autowired
		public AuthUserController(BookstoreUserRegister userRegister, SmsService smsService, JavaMailSender javaMailSender,
				PasswordEncoder passwordEncoder, BookstoreUserRepository bookstoreUserRepository,BalanceTransactionRepository balanceTransactionRepository)
		{
				this.userRegister = userRegister;
				this.smsService = smsService;
				this.javaMailSender = javaMailSender;
				this.passwordEncoder = passwordEncoder;
				this.bookstoreUserRepository = bookstoreUserRepository;
				this.balanceTransactionRepository = balanceTransactionRepository;

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
		public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload payload)
		{
				ContactConfirmationResponse response = new ContactConfirmationResponse();
				response.setResult("true");
				if (payload.getContact().contains("@"))
				{
						return response;
				}
				else
				{
						String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
						smsService.saveNewCode(new SmsCode(smsCodeString, 60)); //expires in 1 min.
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
				BookstoreUser userDetails = (BookstoreUser) userRegister.getCurrentUser();
				List<BalanceTransactionEntity> balanceTransactionEntities = userDetails.getBalanceTransactionEntitiesList();

				System.out.println("------------balanceTransactionEntities = " + balanceTransactionEntities);

				model.addAttribute("curUsr", userDetails);
				model.addAttribute("editForm", new RegistrationForm());
//				model.addAttribute("balanceForm", new BalanceEntity());
				model.addAttribute("balanceTransaction",balanceTransactionEntities);
				model.addAttribute("searchWordDto", searchWordDto);
				return "profile";
		}

		@PostMapping("/profile")
		public String saveProfile(Model model, SearchWordDto searchWordDto,
				@RequestParam(value = "pass", required = false) String pass,
				@RequestParam(value = "value", required = false) Integer value) throws UserAttributesException
		{
				BookstoreUser userDetails = (BookstoreUser) userRegister.getCurrentUser();
				if (pass != null)
				{
						userDetails.setPassword(passwordEncoder.encode(pass));
						bookstoreUserRepository.save(userDetails);
				}
				if (value != null)
				{
						BalanceTransactionEntity balance = new BalanceTransactionEntity();
						balance.setBookstoreUser(userDetails);
						balance.setTime(LocalDate.now());
						balance.setValue(value);
						balance.setDescription("account top-up inside bookshop");
						balanceTransactionRepository.save(balance);
						System.out.println("------balacne = " + balance);
						System.out.println("-------value = " + value);

				}
				model.addAttribute("curUsr", userDetails);
				model.addAttribute("editForm", new RegistrationForm());
				model.addAttribute("balanceForm", new BalanceTransactionEntity());
				//				model.addAttribute("balanceTransaction", new BalanceTransactionEntity());
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
