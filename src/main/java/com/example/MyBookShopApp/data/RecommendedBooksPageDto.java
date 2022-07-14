package com.example.MyBookShopApp.data;

import java.util.List;

public class RecommendedBooksPageDto
{

		private Integer count;
		private List<Book> books;



		public RecommendedBooksPageDto(List<Book> books)
		{
				this.books = books;
				this.count = books.size();
		}

		public Integer getCount()
		{
				return count;
		}

		public void setCount(Integer count)
		{
				this.count = count;
		}

		public List<Book> getBooks()
		{
				return books;
		}

		public void setBooks(List<Book> books)
		{
				this.books = books;
		}
}
