package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
public class BookstoreUserRegister
{

		private final BookstoreUserRepository bookstoreUserRepository;

		private final PasswordEncoder passwordEncoder;

		private final AuthenticationManager authenticationManager;

		private final BookstoreUserDetailsService bookstoreUserDetailsService;

		private final JWTUtil jwtUtil;

		@Autowired
		public BookstoreUserRegister(BookstoreUserRepository bookstoreUserRepository, PasswordEncoder passwordEncoder,
				AuthenticationManager authenticationManager, BookstoreUserDetailsService bookstoreUserDetailsService,
				JWTUtil jwtUtil)
		{
				this.bookstoreUserRepository = bookstoreUserRepository;
				this.passwordEncoder = passwordEncoder;
				this.authenticationManager = authenticationManager;
				this.bookstoreUserDetailsService = bookstoreUserDetailsService;
				this.jwtUtil = jwtUtil;
		}

		public void registerNewUser(RegistrationForm registrationForm)
		{
				if (bookstoreUserRepository.findBookstoreUserByEmail(registrationForm.getEmail()) == null)
				{
						BookstoreUser user = new BookstoreUser();
						user.setName(registrationForm.getName());
						user.setEmail(registrationForm.getEmail());
						user.setPhone(registrationForm.getPhone());
						user.setPassword(passwordEncoder.encode(registrationForm.getPass()));
						bookstoreUserRepository.save(user);
				}
		}

		public ContactConfirmationResponse login(ContactConfirmationPayload payload)
		{
				Authentication authentication =
						authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
								payload.getCode()));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				ContactConfirmationResponse response = new ContactConfirmationResponse();
				response.setResult("true");
				return response;
		}

		public ContactConfirmationResponse jwtLogin(ContactConfirmationPayload payload)
		{
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(payload.getContact(),
						payload.getCode().replaceAll(" ", "")));
				BookstoreUserDetails userDetails =
						(BookstoreUserDetails) bookstoreUserDetailsService.loadUserByUsername(payload.getContact());
				String jwtToken = jwtUtil.generateToken(userDetails);
				ContactConfirmationResponse response = new ContactConfirmationResponse();
				response.setResult(jwtToken);
				return response;
		}

		public Object getCurrentUser()
		{
				Object context = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				System.out.println("----context = " + context);
				//todo info additional info about signin user
				if (context instanceof BookstoreUserDetails)
				{
						BookstoreUserDetails userDetails =
								(BookstoreUserDetails) context;
						return userDetails.getBookstoreUser();
				}else if (context instanceof DefaultOidcUser){
						DefaultOidcUser userDetails = (DefaultOidcUser) context;
						System.out.println("-----------uszerDetails .get info = " + userDetails.getUserInfo());
						return userDetails.getUserInfo();
				}else{
						DefaultOAuth2User userDetails = (DefaultOAuth2User) context;
						return userDetails.getAttributes();
				}
		}
}
