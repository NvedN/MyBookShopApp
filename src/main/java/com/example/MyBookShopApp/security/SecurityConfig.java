package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JWTRequestFilter;
import com.example.MyBookShopApp.service.BookstoreUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final BookstoreUserDetailsService bookstoreUserDetailsService;
    private final JWTRequestFilter filter;

    @Autowired
    public SecurityConfig(BookstoreUserDetailsService bookstoreUserDetailsService, JWTRequestFilter filter) {
        this.bookstoreUserDetailsService = bookstoreUserDetailsService;
        this.filter = filter;
    }

    @Bean
    PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(bookstoreUserDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/my","/profile","/books/*").hasRole("USER")//.authenticated()//.hasRole("USER")
            .antMatchers("/adminDeleteBook").hasRole("ADMIN")
            .and().formLogin()
            .loginPage("/signin").failureUrl("/signin")
            .and().logout().logoutUrl("/logout").logoutSuccessUrl("/signin").deleteCookies("token")
            .and().oauth2Login()
            .and().oauth2Client();
//        https://oauth.vk.com/authorize?client_id=51419692&redirect_uri=http://localhost:8085/my&scope=12
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
    }
}
