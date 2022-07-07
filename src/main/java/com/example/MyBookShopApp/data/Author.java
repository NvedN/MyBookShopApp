package com.example.MyBookShopApp.data;

import java.util.List;

public class Author
{

		private Integer id;

		private String firstLetter;

		private String first_name;

		private String last_name;

		@Override public String toString()
		{
				return "Author{" +
						"id=" + id +
						", firstLetter='" + firstLetter + '\'' +
						", first_name='" + first_name + '\'' +
						", last_name='" + last_name + '\'' +
						'}';
		}

		public Integer getId()
		{
				return id;
		}

		public void setId(Integer id)
		{
				this.id = id;
		}

		public String getFirst_name()
		{
				return first_name;
		}

		public void setFirst_name(String first_name)
		{
				this.first_name = first_name;
		}

		public String getLast_name()
		{
				return last_name;
		}

		public void setLast_name(String last_name)
		{
				this.last_name = last_name;
		}

}
