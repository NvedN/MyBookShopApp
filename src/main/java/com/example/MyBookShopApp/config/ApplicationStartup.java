package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.BookSorted;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.BookSortedRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup
    implements ApplicationListener<ApplicationReadyEvent> {

  private BookSortedRepository bookSortedRepository;
  private BookRepository bookRepository;


  @Autowired
  public ApplicationStartup(BookSortedRepository bookSortedRepository,
      BookRepository bookRepository) {
    this.bookSortedRepository = bookSortedRepository;
    this.bookRepository = bookRepository;
  }


  @Scheduled(cron = "0 1 1 * * *")
  public void calculateRating() {
    System.out.println("----start cron calculate!");
    Pageable nextPage = PageRequest.of(0, 100);
    bookSortedRepository.deleteAll();
    do {
      Page<Book> allBooks = bookRepository.findAll(nextPage);
      TreeMap<Double, ArrayList<Book>> popularList = new TreeMap<>();
      for (Book book : allBooks.getContent()) {
        Integer bought = book.getNumberOfBought(); // b
        Integer cart = book.getNumberInCart(); // c
        Integer delayed = book.getNumberDelayed(); // k
        Double P = bought + 0.7 * cart + 0.4 * delayed;
        ArrayList<Book> alreadyMarked = new ArrayList<>();
        if (popularList.containsKey(P)) {
          alreadyMarked = popularList.get(P);
          alreadyMarked.add(book);
        } else {
          alreadyMarked.add(book);
        }
        popularList.put(P, alreadyMarked);
      }
      //				P = B + 0,7*C + 0,4*K,
      //						где B — количество пользователей, купивших книгу, C — количество пользователей, у
      // которых книга находится в корзине, а K — количество пользователей, у которых книга отложена.

      for (Double rating : popularList.keySet()) {
        List<Book> books = popularList.get(rating);
        for (Book book : books) {
          BookSorted bookSorted = new BookSorted();
          bookSorted.setRating(rating);
          bookSorted.setBook(book);
          bookSortedRepository.save(bookSorted);
        }
      }
      nextPage = allBooks.nextPageable();
    } while (nextPage.isPaged());
  }

  /**
   * This event is executed as late as conceivably possible to indicate that the application is
   * ready to service requests.
   */
  @Override
  public void onApplicationEvent(final ApplicationReadyEvent event) {

    calculateRating();
  }


  @PreDestroy
  public void destroy() {
    System.out.println(
        "Callback triggered - @PreDestroy.");
    bookSortedRepository.deleteAll();
  }
}