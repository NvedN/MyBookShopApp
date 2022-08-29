package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.entity.book.file.BookFileEntity;
import com.example.MyBookShopApp.data.entity.book.file.BookFileRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class ResourceStorage
{

		@Value("${upload.path}")
		String uploadPath;

		@Value("${download.path}")
		String downloadPath;

		private final BookFileRepository bookFileRepository;

		@Autowired
		public ResourceStorage(BookFileRepository bookFileRepository) {
				this.bookFileRepository = bookFileRepository;
		}

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

		public Path getBookFilePath(String hash) {
				BookFileEntity bookFile = bookFileRepository.findBookFileByHash(hash);
				return Paths.get(bookFile.getPath());
		}

		public MediaType getBookFileMime(String hash) {
				BookFileEntity bookFile = bookFileRepository.findBookFileByHash(hash);
				String mimeType =
						URLConnection.guessContentTypeFromName(Paths.get(bookFile.getPath()).getFileName().toString());

				if (mimeType != null) {
						return MediaType.parseMediaType(mimeType);
				}else {
						return MediaType.APPLICATION_OCTET_STREAM;
				}
		}

		public byte[] getBookFileByteArray(String hash) throws IOException {
				BookFileEntity bookFile = bookFileRepository.findBookFileByHash(hash);
				Path path = Paths.get(downloadPath, bookFile.getPath());
				return Files.readAllBytes(path);

		}
}
