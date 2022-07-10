package com.example.MyBookShopApp.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthorService
{

		private  static JdbcTemplate jdbcTemplate;

		@Autowired
		public AuthorService(JdbcTemplate jdbcTemplate) {
				this.jdbcTemplate = jdbcTemplate;
		}

		public List<Author> getAuthorsData()
		{
				return jdbcTemplate.query("SELECT * FROM authors",new BeanPropertyRowMapper<>(Author.class));

		}

		public static List<Author> getSortedAuthorsByFirstLetter(String letter,List<Author>authorsList ){
				HashMap<String,List<Author>> outputSortedMap = new HashMap<>();
//				List<Author> authorsList = jdbcTemplate.query("SELECT * FROM authors",new BeanPropertyRowMapper<>(Author.class));
				for(Author author: authorsList){
						String firstLetter = String.valueOf(author.getLast_name().charAt(0));
						if (outputSortedMap.containsKey(firstLetter)){
								List<Author> tmp = outputSortedMap.get(firstLetter);
								tmp.add(author);
								outputSortedMap.replace(firstLetter,tmp);

						}else{
								List<Author> tmp = null;
								tmp.add(author);
								outputSortedMap.put(firstLetter,tmp);
						}
				}

				return outputSortedMap.get(letter);
		}



		public static HashSet firstLetterData(){
				List<Author> authorsList = jdbcTemplate.query("SELECT * FROM authors",new BeanPropertyRowMapper<>(Author.class));
				HashSet<String> output = new HashSet();
				for(Author author: authorsList){
						String firstLetter = String.valueOf(author.getLast_name().charAt(0));
						output.add(firstLetter);
				}
				return output;
		}


		public static List<Author> sortAuthorsByFirstLetter(List<Author> authorsList) throws Exception{
				List<String> list = new ArrayList<String>();
				System.out.println("---------authorsList beforeSort = " + authorsList);

				Collections.sort(authorsList, new Comparator<Author>() {

						@Override public int compare(Author o1, Author o2)
						{
								String lastName1 = o1.getLast_name();
								String lastName2 = o2.getLast_name();

								return String.valueOf(lastName1.charAt(0)).compareTo(String.valueOf(lastName2.charAt(0)));
						}

				});
				System.out.println("---------authorsList aftersort = " + authorsList);

				return authorsList;
		}




}
