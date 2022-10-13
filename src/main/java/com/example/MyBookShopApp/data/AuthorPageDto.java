package com.example.MyBookShopApp.data;

import java.util.List;

public class AuthorPageDto
{

		private Integer count;
		private List<Author> authors;

		public AuthorPageDto(List<Author> authors) {
				this.count = authors.size();
				this.authors = authors;
		}

		public AuthorPageDto(){

		}

		public Integer getCount() {
				return count;
		}

		public void setCount(Integer count) {
				this.count = count;
		}

	public List<Author> getAuthors() {
		return authors;
	}

}
