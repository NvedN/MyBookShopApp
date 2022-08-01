package com.example.MyBookShopApp.data;

import java.util.List;

public class BooksPageDto
{

		private Integer count;
		private List<Book> books;

		public BooksPageDto(List<Book> books) {
				this.count = books.size();
				this.books = books;
		}

		public Integer getCount() {
				return count;
		}

		public void setCount(Integer count) {
				this.count = count;
		}

		public List<Book> getBooks() {
				return books;
		}

		public void setBooks(List<Book> books) {
				this.books = books;
		}
}
