package com.sfcollection.service;

import com.sfcollection.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDTO createBook(BookCreateDTO bookCreateDTO);
    BookDTO getBookById(Long id);
    Page<BookDTO> getAllBooks(Pageable pageable);
    Page<BookDTO> searchBooks(BookSearchDTO searchDTO, Pageable pageable);
    BookDTO updateBook(Long id, BookUpdateDTO bookUpdateDTO);
    BookDTO patchBook(Long id, BookPatchDTO bookPatchDTO);
    void deleteBook(Long id);
    BookDTO addAuthorToBook(Long bookId, Long authorId);
    BookDTO removeAuthorFromBook(Long bookId, Long authorId);
    Page<BookDTO> getBooksByAuthor(Long authorId, Pageable pageable);
    Page<BookDTO> getBooksByCollection(Long collectionId, Pageable pageable);
}