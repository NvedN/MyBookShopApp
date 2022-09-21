package com.example.MyBookShopApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer
{
		@Value("${upload.path}")
		private String uploadPath;

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry){
				registry.addResourceHandler("/customImg/**").addResourceLocations("file:"+uploadPath+"/");
		}


		@Bean
		public RestTemplate getRestTemplate(){
				return new RestTemplate();
		}
}
