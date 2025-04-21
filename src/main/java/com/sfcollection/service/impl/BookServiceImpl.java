package com.sfcollection.service.impl;

import com.sfcollection.dto.*;
import com.sfcollection.exception.ResourceNotFoundException;
import com.sfcollection.mapper.BookMapper;
import com.sfcollection.model.Author;
import com.sfcollection.model.Book;
import com.sfcollection.repository.AuthorRepository;
import com.sfcollection.repository.BookRepository;
import com.sfcollection.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookMapper bookMapper;
    
    @Override
    @Transactional
    public BookDTO createBook(BookCreateDTO bookCreateDTO) {
        // Validate unique ISBN if provided
        if (StringUtils.hasText(bookCreateDTO.getIsbn()) && bookRepository.existsByIsbn(bookCreateDTO.getIsbn())) {
            throw new IllegalArgumentException("A book with ISBN " + bookCreateDTO.getIsbn() + " already exists");
        }
        
        Book book = bookMapper.createDtoToEntity(bookCreateDTO);
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
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return bookMapper.toDtoPage(books);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> searchBooks(BookSearchDTO searchDTO, Pageable pageable) {
        // Check if we need to use author or collection specific search
        if (searchDTO.getAuthorId() != null) {
            return getBooksByAuthor(searchDTO.getAuthorId(), pageable);
        }
        
        if (searchDTO.getCollectionId() != null) {
            return getBooksByCollection(searchDTO.getCollectionId(), pageable);
        }
        
        // Use the advanced search query
        Page<Book> books = bookRepository.searchBooks(
                searchDTO.getTitle(),
                searchDTO.getIsbn(),
                searchDTO.getSubGenre(),
                searchDTO.getPublishedAfter(),
                searchDTO.getPublishedBefore(),
                pageable
        );
        
        return bookMapper.toDtoPage(books);
    }
    
    @Override
    @Transactional
    public BookDTO updateBook(Long id, BookUpdateDTO bookUpdateDTO) {
        Book existingBook = findBookById(id);
        
        // Validate unique ISBN if changed
        if (StringUtils.hasText(bookUpdateDTO.getIsbn()) && 
                !Objects.equals(existingBook.getIsbn(), bookUpdateDTO.getIsbn()) && 
                bookRepository.existsByIsbn(bookUpdateDTO.getIsbn())) {
            throw new IllegalArgumentException("A book with ISBN " + bookUpdateDTO.getIsbn() + " already exists");
        }
        
        // Copy properties from DTO to entity
        bookMapper.updateDtoToEntity(bookUpdateDTO, existingBook);
        
        Book updatedBook = bookRepository.save(existingBook);
        return bookMapper.toDto(updatedBook);
    }
    
    @Override
    @Transactional
    public BookDTO patchBook(Long id, BookPatchDTO bookPatchDTO) {
        Book existingBook = findBookById(id);
        
        // Validate unique ISBN if changed
        if (StringUtils.hasText(bookPatchDTO.getIsbn()) && 
                !Objects.equals(existingBook.getIsbn(), bookPatchDTO.getIsbn()) && 
                bookRepository.existsByIsbn(bookPatchDTO.getIsbn())) {
            throw new IllegalArgumentException("A book with ISBN " + bookPatchDTO.getIsbn() + " already exists");
        }
        
        // Apply partial updates
        bookMapper.patchDtoToEntity(bookPatchDTO, existingBook);
        
        Book updatedBook = bookRepository.save(existingBook);
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
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> getBooksByAuthor(Long authorId, Pageable pageable) {
        // Verify the author exists
        if (!authorRepository.existsById(authorId)) {
            throw new ResourceNotFoundException("Author not found with id: " + authorId);
        }
        
        Page<Book> books = bookRepository.findByAuthorsId(authorId, pageable);
        return bookMapper.toDtoPage(books);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<BookDTO> getBooksByCollection(Long collectionId, Pageable pageable) {
        Page<Book> books = bookRepository.findByCollectionsId(collectionId, pageable);
        return bookMapper.toDtoPage(books);
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