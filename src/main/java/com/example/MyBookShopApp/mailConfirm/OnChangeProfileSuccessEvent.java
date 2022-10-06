package com.example.MyBookShopApp.mailConfirm;

import com.example.MyBookShopApp.security.BookstoreUser;
import org.springframework.context.ApplicationEvent;

public class OnChangeProfileSuccessEvent extends ApplicationEvent {


  private static final long serialVersionUID = 1L;
  private String appUrl;
  private BookstoreUser user;

  public OnChangeProfileSuccessEvent(BookstoreUser user, String appUrl) {
    super(user);
    this.user = user;
    this.appUrl = appUrl;
  }

  public String getAppUrl() {
    return appUrl;
  }

  public void setAppUrl(String appUrl) {
    this.appUrl = appUrl;
  }

  public BookstoreUser getUser() {
    return user;
  }

  public void setUser(BookstoreUser user) {
    this.user = user;
  }
}
