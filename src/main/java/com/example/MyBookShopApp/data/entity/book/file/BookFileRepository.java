package com.example.MyBookShopApp.data.entity.book.file;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookFileRepository extends JpaRepository<BookFileEntity, Integer> {

    public BookFileEntity findBookFileByHash(String hash);
}
