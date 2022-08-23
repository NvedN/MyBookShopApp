package com.example.MyBookShopApp.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class ResourceStorage
{

		@Value("${upload.path}")
		String uploadPath;

		public String saveNewBookImage(MultipartFile file, String slug) throws IOException
		{
				System.out.println("-------uploadPath = " + uploadPath);
				System.out.println("--------test w1 ");
				System.out.println("Paths.get(uploadPath) = " + Paths.get(uploadPath));
				System.out.println("----------test = !");
				System.out.println("new File(uploadPath).exists() = " + new File(uploadPath).exists());
				String resourceURI = null;
				if (!file.isEmpty())
				{
						if (!new File(uploadPath).exists())
						{
								Files.createDirectories(Paths.get(uploadPath));
								Logger.getLogger(this.getClass().getSimpleName()).info("Create image folder in " + uploadPath);
						}

						String fileName = slug+"."+ FilenameUtils.getExtension(file.getOriginalFilename());
						Path path = Paths.get(uploadPath,fileName);
						resourceURI = uploadPath + "/"+fileName;
						file.transferTo(path); //uploading user file here
						Logger.getLogger(this.getClass().getSimpleName()).info(fileName+"uploaded OK!");
				}
				return resourceURI;
		}
}
