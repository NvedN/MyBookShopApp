package com.example.MyBookShopApp.data.entity.book.file;

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
