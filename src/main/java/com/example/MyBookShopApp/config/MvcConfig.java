package com.example.MyBookShopApp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class MvcConfig implements WebMvcConfigurer
{
		@Value("${upload.path}")
		private String uploadPath;

		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry){
				registry.addResourceHandler("/customImg/**").addResourceLocations("file:"+uploadPath+"/");
		}
}
