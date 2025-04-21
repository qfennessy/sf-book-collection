package com.sfcollection.service.impl;

import com.sfcollection.dto.AuthorDTO;
import com.sfcollection.exception.ResourceNotFoundException;
import com.sfcollection.mapper.AuthorMapper;
import com.sfcollection.model.Author;
import com.sfcollection.model.Book;
import com.sfcollection.repository.AuthorRepository;
import com.sfcollection.repository.BookRepository;
import com.sfcollection.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final AuthorMapper authorMapper;
    
    @Override
    @Transactional
    public AuthorDTO createAuthor(AuthorDTO authorDTO) {
        Author author = authorMapper.toEntity(authorDTO);
        Author savedAuthor = authorRepository.save(author);
        return authorMapper.toDto(savedAuthor);
    }
    
    @Override
    @Transactional(readOnly = true)
    public AuthorDTO getAuthorById(Long id) {
        Author author = findAuthorById(id);
        return authorMapper.toDto(author);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authorMapper.toDtoList(authors);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<AuthorDTO> searchAuthorsByName(String name) {
        List<Author> authors = authorRepository.findByNameContainingIgnoreCase(name);
        return authorMapper.toDtoList(authors);
    }
    
    @Override
    @Transactional
    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        Author existingAuthor = findAuthorById(id);
        
        existingAuthor.setName(authorDTO.getName());
        existingAuthor.setBiography(authorDTO.getBiography());
        existingAuthor.setBirthDate(authorDTO.getBirthDate());
        existingAuthor.setPhotoUrl(authorDTO.getPhotoUrl());
        
        Author updatedAuthor = authorRepository.save(existingAuthor);
        return authorMapper.toDto(updatedAuthor);
    }
    
    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public AuthorDTO addBookToAuthor(Long authorId, Long bookId) {
        Author author = findAuthorById(authorId);
        Book book = findBookById(bookId);
        
        author.getBooks().add(book);
        book.getAuthors().add(author);
        
        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.toDto(updatedAuthor);
    }
    
    @Override
    @Transactional
    public AuthorDTO removeBookFromAuthor(Long authorId, Long bookId) {
        Author author = findAuthorById(authorId);
        Book book = findBookById(bookId);
        
        author.getBooks().remove(book);
        book.getAuthors().remove(author);
        
        Author updatedAuthor = authorRepository.save(author);
        return authorMapper.toDto(updatedAuthor);
    }
    
    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }
    
    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}