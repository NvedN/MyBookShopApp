package com.example.MyBookShopApp.data;

import javax.persistence.*;

@Entity
@Table(name = "test_entities")
public class TestEntity
{
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		private String data;

		public Long getId()
		{
				return id;
		}

		public void setId(Long id)
		{
				this.id = id;
		}

		public String getData()
		{
				return data;
		}

		public void setData(String data)
		{
				this.data = data;
		}

		@Override public String toString()
		{
				return "TestEntity{" +
						"id=" + id +
						", data='" + data + '\'' +
						'}';
		}
}
