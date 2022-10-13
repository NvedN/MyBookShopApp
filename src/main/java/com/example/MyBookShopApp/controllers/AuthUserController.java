package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.SmsCode;
import com.example.MyBookShopApp.data.entity.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.data.entity.payments.BalanceTransactionRepository;
import com.example.MyBookShopApp.data.entity.user.BookstoreUser;
import com.example.MyBookShopApp.exceptions.UserAttributesException;
import com.example.MyBookShopApp.security.BookstoreUserRegister;
import com.example.MyBookShopApp.security.BookstoreUserRepository;
import com.example.MyBookShopApp.security.ContactConfirmationPayload;
import com.example.MyBookShopApp.security.ContactConfirmationResponse;
import com.example.MyBookShopApp.security.RegistrationForm;
import com.example.MyBookShopApp.security.jwt.JWTUtil;
import com.example.MyBookShopApp.service.BookstoreUserDetailsService;
import com.example.MyBookShopApp.service.SmsService;
import com.example.MyBookShopApp.util.Util;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@Controller
public class AuthUserController {

  private final BookstoreUserRegister userRegister;

  private final SmsService smsService;

  private final JavaMailSender javaMailSender;

  private final PasswordEncoder passwordEncoder;

  private final BookstoreUserRepository bookstoreUserRepository;

  private final BalanceTransactionRepository balanceTransactionRepository;

  private final JWTUtil jwtUtil;

  private final BookstoreUserDetailsService bookstoreUserDetailsService;

  private MessageSource messages;

  @Autowired
  public AuthUserController(
      BookstoreUserRegister userRegister,
      SmsService smsService,
      JavaMailSender javaMailSender,
      PasswordEncoder passwordEncoder,
      BookstoreUserRepository bookstoreUserRepository,
      BalanceTransactionRepository balanceTransactionRepository,
      JWTUtil jwtUtil,
      BookstoreUserDetailsService bookstoreUserDetailsService,
      MessageSource messages) {
    this.userRegister = userRegister;
    this.smsService = smsService;
    this.javaMailSender = javaMailSender;
    this.passwordEncoder = passwordEncoder;
    this.bookstoreUserRepository = bookstoreUserRepository;
    this.balanceTransactionRepository = balanceTransactionRepository;
    this.jwtUtil = jwtUtil;
    this.bookstoreUserDetailsService = bookstoreUserDetailsService;
    this.messages = messages;
  }

  @GetMapping("/signin")
  public String handleSignin() {
    return "signin";
  }

  @GetMapping("/signup")
  public String handleSignUp(Model model, SearchWordDto searchWordDto) {
    model.addAttribute("regForm", new RegistrationForm());
    model.addAttribute("searchWordDto", searchWordDto);
    return "signup";
  }

  @PostMapping("/requestContactConfirmation")
  @ResponseBody
  public ContactConfirmationResponse handleRequestContactConfirmation(
      @RequestBody ContactConfirmationPayload payload) {
    ContactConfirmationResponse response = new ContactConfirmationResponse();
    response.setResult("true");
    if (payload.getContact().contains("@")) {
      return response;
    } else {
      String smsCodeString = smsService.sendSecretCodeSms(payload.getContact());
      smsService.saveNewCode(new SmsCode(smsCodeString, 60)); // expires in 1 min.
      return response;
    }
  }

  @PostMapping("/requestEmailConfirmation")
  @ResponseBody
  public ContactConfirmationResponse handleRequestEmailConfirmation(
      @RequestBody ContactConfirmationPayload payload) {
    ContactConfirmationResponse response = new ContactConfirmationResponse();
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom("mybook.shopapp@bk.ru");
    message.setTo(payload.getContact());
    SmsCode smsCode = new SmsCode(smsService.generateCode(), 300); // 5 minutes
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
  public ContactConfirmationResponse handleApproveContact(
      @RequestBody ContactConfirmationPayload payload) {
    ContactConfirmationResponse response = new ContactConfirmationResponse();
    response.setResult("true");
    return response;
  }

  @PostMapping("/reg")
  public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
    userRegister.registerNewUser(registrationForm);
    model.addAttribute("regOk", true);
    return "signin";
  }

  @PostMapping("/login")
  @ResponseBody
  public ContactConfirmationResponse handleLogin(
      @RequestBody ContactConfirmationPayload payload, HttpServletResponse httpServletResponse) {
    ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
    Cookie cookie = new Cookie("token", loginResponse.getResult());
    httpServletResponse.addCookie(cookie);
    return loginResponse;
  }

  @GetMapping("/my")
  public String handleMy(SearchWordDto searchWordDto, Model model) {
    model.addAttribute("searchWordDto", searchWordDto);
    return "my";
  }

  @GetMapping("/profile")
  public String handleProfile(Model model, SearchWordDto searchWordDto)
      throws UserAttributesException {
    BookstoreUser userDetails = (BookstoreUser) userRegister.getCurrentUser();
    List<BalanceTransactionEntity> balanceTransactionEntities =
        userDetails.getBalanceTransactionEntitiesList();
    model.addAttribute("curUsr", userDetails);
    model.addAttribute("editForm", new RegistrationForm());
    model.addAttribute("balanceForm", new BalanceTransactionEntity());
    model.addAttribute("balanceTransaction", balanceTransactionEntities);
    model.addAttribute("searchWordDto", searchWordDto);
    return "profile";
  }

  //  @PostMapping("/profileSave")
  @PostMapping("/profile")
  public String saveProfile(
      Model model,
      SearchWordDto searchWordDto,
      @RequestParam(value = "password", required = false) String pass,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "phone", required = false) String phone,
      @RequestParam(value = "value", required = false) Integer value)
      throws UserAttributesException {
    BookstoreUser userDetails = (BookstoreUser) userRegister.getCurrentUser();
    if (pass != null) {
      pass = pass.replaceAll(",", ""); // only for bad html file
    }else{
      pass = "";
    }
    boolean isEdited = false;
    if (!pass.equals("")) {
      userDetails.setPassword(passwordEncoder.encode(pass));
      isEdited = true;
    }
    if (name != null && !userDetails.getName().equals(name)) {
      userDetails.setName(name);
      isEdited = true;
    }
    if (email != null && !userDetails.getEmail().equals(email)) {
      userDetails.setEmail(email);
      isEdited = true;
    }
    if (phone != null && !userDetails.getPhone().equals(phone)) {
      userDetails.setPhone(phone);
      isEdited = true;
    }

    if (isEdited) {
      String jwtToken = jwtUtil.generateTokenDetails(userDetails);
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom("mybook.shopapp@bk.ru");
      message.setTo(email);
      message.setSubject("Bookstore change profile conformation!");
      message.setText(
          "Verification link is: http://localhost:8085/conformationCheck?token=" + jwtToken);
      javaMailSender.send(message);
      System.out.println("------javaMailSender = " + javaMailSender);
    }

    if (value != null) {
      BalanceTransactionEntity balance = new BalanceTransactionEntity();
      balance.setBookstoreUser(userDetails);
      balance.setTime(LocalDate.now());
      balance.setValue(value);
      balance.setDescription("account top-up inside bookshop");
      balanceTransactionRepository.save(balance);
    }
    model.addAttribute("curUsr", userDetails);
    model.addAttribute("balanceForm", new BalanceTransactionEntity());
    model.addAttribute("searchWordDto", searchWordDto);
    return "redirect:/profile";
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

  @GetMapping("/conformationCheck")
  public String confirmRegistration(
      WebRequest request, Model model, @RequestParam("token") String token)
      throws UserAttributesException {
    ArrayList<String> params =
        Util.split(
            jwtUtil.extractUsername(token), ","); // 0 - Name, 1 - email, 2 - pass , 3 - phone
    BookstoreUser userDetails = (BookstoreUser) userRegister.getCurrentUser();
    userDetails.setName(params.get(0));
    userDetails.setEmail(params.get(1));
    userDetails.setPassword(params.get(2));
    bookstoreUserRepository.save(userDetails);
    return "redirect:/profile";
  }
}
