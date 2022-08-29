package com.example.MyBookShopApp.data.entity.book.file;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookFileType;

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

		@Column(name = "type_id")
		private Integer typeId;

		@ManyToOne
		@JoinColumn(name = "book_id", referencedColumnName = "id")
		private Book book;

		public String getBookFileExtensionString(){
				return BookFileType.getExtensionStringByTypeID(typeId);
		}

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

		public Integer getTypeId()
		{
				return typeId;
		}

		public void setTypeId(Integer typeId)
		{
				this.typeId = typeId;
		}
}
