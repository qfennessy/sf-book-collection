package com.sfcollection.service;

import com.sfcollection.dto.AuthorDTO;

import java.util.List;
import java.util.Set;

public interface AuthorService {
    AuthorDTO createAuthor(AuthorDTO authorDTO);
    AuthorDTO getAuthorById(Long id);
    List<AuthorDTO> getAllAuthors();
    List<AuthorDTO> searchAuthorsByName(String name);
    AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO);
    void deleteAuthor(Long id);
    AuthorDTO addBookToAuthor(Long authorId, Long bookId);
    AuthorDTO removeBookFromAuthor(Long authorId, Long bookId);
}