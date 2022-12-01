package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.entity.user.BookstoreUser;
import com.example.MyBookShopApp.util.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class BookstoreUserDetails implements UserDetails {

    private final BookstoreUser bookstoreUser;

    public BookstoreUserDetails(BookstoreUser bookstoreUser) {
        this.bookstoreUser = bookstoreUser;
    }

    public BookstoreUser getBookstoreUser() {
        return bookstoreUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public Collection<? extends GrantedAuthority> getAuthoritiesAdmin() {
        ArrayList<String> roles = new ArrayList<>(Arrays.asList(bookstoreUser.getRoles().split(",")));
        if(roles.contains("ADMIN")) {
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }else{
            return null;
        }
    }

    @Override
    public String getPassword() {
        return bookstoreUser.getPassword();
    }

    @Override
    public String getUsername() {
        return bookstoreUser.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public ArrayList<String> getRoles(){
        return new ArrayList<>(Arrays.asList(bookstoreUser.getRoles().split(",")));
    }


    @Override
    public String toString() {
        return "BookstoreUserDetails{" +
            "bookstoreUser=" + bookstoreUser +
            '}';
    }
}
