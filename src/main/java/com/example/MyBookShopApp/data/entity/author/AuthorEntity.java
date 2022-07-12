package com.example.MyBookShopApp.data.entity.author;

import javax.persistence.*;

@Entity
@Table(name = "author")
public class AuthorEntity
{
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;

		private String photo ;
		private String 	slug ;
		private String  name ;
		private String 	description ;

		public Integer getId()
		{
				return id;
		}

		public void setId(Integer id)
		{
				this.id = id;
		}

		public String getPhoto()
		{
				return photo;
		}

		public void setPhoto(String photo)
		{
				this.photo = photo;
		}

		public String getSlug()
		{
				return slug;
		}

		public void setSlug(String slug)
		{
				this.slug = slug;
		}

		public String getName()
		{
				return name;
		}

		public void setName(String name)
		{
				this.name = name;
		}

		public String getDescription()
		{
				return description;
		}

		public void setDescription(String description)
		{
				this.description = description;
		}
}
