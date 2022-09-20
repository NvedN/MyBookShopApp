package com.example.MyBookShopApp.aspect;

import com.example.MyBookShopApp.exceptions.EmptySearchException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TestAspect
{

		@Pointcut(value = "")
		public void throwCustomException()
		{
		}

		@AfterThrowing(pointcut = "execution(* com.example.MyBookShopApp.service.BookstoreUserDetailsService.*(..))", throwing = "ex")
		public void logAfterThrowingAllMethods(UsernameNotFoundException ex) throws Throwable
		{
				System.out.println("****LoggingAspect.logAfterThrowingAllMethods() " + ex);
		}

		@AfterThrowing(pointcut = "execution(* com.example.MyBookShopApp.controllers.SearchController.*(..))", throwing = "ex")
		public void logAfterThrowingAllMethods1(EmptySearchException ex) throws Throwable
		{
				System.out.println("****LoggingAspect.logAfterThrowingAllMethods1() " + ex);
		}
}
