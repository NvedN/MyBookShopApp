package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BooksRatingAndPopularityService
{

		private BookRepository bookRepository;

		@Autowired
		public BooksRatingAndPopularityService(BookRepository bookRepository)
		{
				this.bookRepository = bookRepository;
		}

		public List<Book> findPopularsBooks(Integer offset, Integer limit)
		{
				List<Book> allBooks = bookRepository.findAll();
				HashMap<Double, ArrayList<Book>> popularList = new HashMap<>();
				for (Book book : allBooks)
				{
						Integer bought = book.getNumberOfBought(); //b
						Integer cart = book.getNumberInCart();//c
						Integer delayed = book.getNumberDelayed();//k
						Double P = bought + 0.7 * cart + 0.4 * delayed;
						ArrayList<Book> alreadyMarked = new ArrayList<>();
						if (popularList.containsKey(P))
						{
								alreadyMarked = popularList.get(P);
						}
						if (alreadyMarked.size() <= limit)
						{
								alreadyMarked.add(book);
								popularList.put(P, alreadyMarked);
						}
				}
				//				P = B + 0,7*C + 0,4*K,
				//						где B — количество пользователей, купивших книгу, C — количество пользователей, у которых книга находится в корзине, а K — количество пользователей, у которых книга отложена.
				HashMap<Double, List<Book>> myNewMap = popularList.entrySet().stream()
						.limit(limit)
						.collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
				HashMap<Double, List<Book>> sortedOutput = new HashMap<>();
				myNewMap.entrySet()
						.stream()
						.sorted(Map.Entry.comparingByKey())
						.forEachOrdered(x -> sortedOutput.put(x.getKey(), x.getValue()));
				ArrayList<Book> outputMap = new ArrayList<>();
				for (Double rating : sortedOutput.keySet())
				{
						outputMap.addAll(sortedOutput.get(rating));
				}
				return outputMap;
		}
}
