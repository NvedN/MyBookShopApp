package com.example.MyBookShopApp.mailConfirm;

import com.example.MyBookShopApp.security.BookstoreUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Component;

@Component
public class changeProfileListener implements ApplicationListener<OnChangeProfileSuccessEvent> {

  @Autowired
  private IUserService userService;
  @Autowired
  private MailSender mailSender;

  @Override
  public void onApplicationEvent(OnChangeProfileSuccessEvent event) {

  }


  BookstoreUser user = event.getUser();
  String token = UUID.randomUUID().toString();
  userService.createVerificationToken(user,token);


//
//  @Override
//  public void onApplicationEvent(OnChangeProfileSuccessEvent event) {
//    this.confirmRegistration(event);
//  }

}
