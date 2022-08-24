package com.example.MyBookShopApp.data.entity.book.file;

import com.example.MyBookShopApp.data.Book;

import javax.persistence.*;

@Entity
@Table(name = "book_file")
public class BookFileEntity
{

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;
		private String hash;
		private String path;
		private String type_id;

		@ManyToOne
		@JoinColumn(name = "book_id", referencedColumnName = "id")
		private Book book;

		public Book getBook()
		{
				return book;
		}

		public void setBook(Book book)
		{
				this.book = book;
		}

		public Integer getId()
		{
				return id;
		}

		public void setId(Integer id)
		{
				this.id = id;
		}

		public String getHash()
		{
				return hash;
		}

		public void setHash(String hash)
		{
				this.hash = hash;
		}

		public String getPath()
		{
				return path;
		}

		public void setPath(String path)
		{
				this.path = path;
		}

		public String getType_id()
		{
				return type_id;
		}

		public void setType_id(String type_id)
		{
				this.type_id = type_id;
		}
}
