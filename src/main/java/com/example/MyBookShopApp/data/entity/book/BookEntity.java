package com.example.MyBookShopApp.data.entity.book;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "book")
public class BookEntity
{

		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Integer id;

		private String description;

		private Integer smallint;

		private String image;

		private String is_bestseller;

		private Integer price;

		@DateTimeFormat
		private LocalDate pub_date;

		private String slug;

		private String title;

		public Integer getId()
		{
				return id;
		}

		public void setId(Integer id)
		{
				this.id = id;
		}

		public String getDescription()
		{
				return description;
		}

		public void setDescription(String description)
		{
				this.description = description;
		}

		public Integer getSmallint()
		{
				return smallint;
		}

		public void setSmallint(Integer smallint)
		{
				this.smallint = smallint;
		}

		public String getImage()
		{
				return image;
		}

		public void setImage(String image)
		{
				this.image = image;
		}

		public String getIs_bestseller()
		{
				return is_bestseller;
		}

		public void setIs_bestseller(String is_bestseller)
		{
				this.is_bestseller = is_bestseller;
		}

		public Integer getPrice()
		{
				return price;
		}

		public void setPrice(Integer price)
		{
				this.price = price;
		}

		public LocalDate getPub_date()
		{
				return pub_date;
		}

		public void setPub_date(LocalDate pub_date)
		{
				this.pub_date = pub_date;
		}

		public String getSlug()
		{
				return slug;
		}

		public void setSlug(String slug)
		{
				this.slug = slug;
		}

		public String getTitle()
		{
				return title;
		}

		public void setTitle(String title)
		{
				this.title = title;
		}
}
