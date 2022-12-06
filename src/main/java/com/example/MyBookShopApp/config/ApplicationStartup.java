package com.example.MyBookShopApp.config;

import com.example.MyBookShopApp.data.Book;
import com.example.MyBookShopApp.data.entity.book.BookSorted;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.BookSortedRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
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


  @Scheduled(cron = "0 0 10 * * MON-FRI")
  public void calculateRating() {
    System.out.println("----start cron calculate!");
    List<Book> allBooks = bookRepository.findAll();
    TreeMap<Double, ArrayList<Book>> popularList = new TreeMap<>();
    for (Book book : allBooks) {
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

    ArrayList<Book> outputMap = new ArrayList<>();
    List<BookSorted> booksSortedList = bookSortedRepository.findAll();
    for (Double rating : popularList.keySet()) {
      List<Book> books = popularList.get(rating);
      for (Book book : books) {

        Optional<BookSorted> bookSorted =
            booksSortedList.stream()
                .filter(b -> b.getBook().getId().equals(book.getId()))
                .findAny();
//                      .orElse(new BookSorted());
        if (bookSorted.isPresent()) {
          bookSorted.get().setRating(rating);
          bookSortedRepository.save(bookSorted.get());
        } else {
          BookSorted bookSortedNew = new BookSorted();
          bookSortedNew.setRating(rating);

          bookSortedNew.setBook(book);
          bookSortedRepository.save(bookSortedNew);

        }
      }
      outputMap.addAll(popularList.get(rating));
    }
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