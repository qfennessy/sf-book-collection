package com.sfcollection.service.impl;

import com.sfcollection.dto.BookDTO;
import com.sfcollection.exception.ResourceNotFoundException;
import com.sfcollection.mapper.BookMapper;
import com.sfcollection.model.Author;
import com.sfcollection.model.Book;
import com.sfcollection.repository.AuthorRepository;
import com.sfcollection.repository.BookRepository;
import com.sfcollection.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    
    @Override
    @Transactional
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = bookMapper.toEntity(bookDTO);
        Book savedBook = bookRepository.save(book);
        return bookMapper.toDto(savedBook);
    }
    
    @Override
    @Transactional(readOnly = true)
    public BookDTO getBookById(Long id) {
        Book book = findBookById(id);
        return bookMapper.toDto(book);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return bookMapper.toDtoList(books);
    }
    
    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = findBookById(id);
        
        // Keep the existing authors and id/dateAdded
        bookDTO.setId(existingBook.getId());
        bookDTO.setDateAdded(existingBook.getDateAdded());
        
        // Copy properties from DTO to entity, but avoid overriding the collections/relations
        Book bookToUpdate = bookMapper.toEntity(bookDTO);
        bookToUpdate.setAuthors(existingBook.getAuthors());
        
        Book updatedBook = bookRepository.save(bookToUpdate);
        return bookMapper.toDto(updatedBook);
    }
    
    @Override
    @Transactional
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        
        bookRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public BookDTO addAuthorToBook(Long bookId, Long authorId) {
        Book book = findBookById(bookId);
        Author author = findAuthorById(authorId);
        
        book.getAuthors().add(author);
        author.getBooks().add(book);
        
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }
    
    @Override
    @Transactional
    public BookDTO removeAuthorFromBook(Long bookId, Long authorId) {
        Book book = findBookById(bookId);
        Author author = findAuthorById(authorId);
        
        book.getAuthors().remove(author);
        author.getBooks().remove(book);
        
        Book updatedBook = bookRepository.save(book);
        return bookMapper.toDto(updatedBook);
    }
    
    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
    
    private Author findAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }
}