package com.sfcollection.service.impl;

import com.sfcollection.dto.BookDTO;
import com.sfcollection.exception.ResourceNotFoundException;
import com.sfcollection.model.Book;
import com.sfcollection.repository.BookRepository;
import com.sfcollection.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository;
    
    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);
        Book savedBook = bookRepository.save(book);
        
        BookDTO savedBookDTO = new BookDTO();
        BeanUtils.copyProperties(savedBook, savedBookDTO);
        return savedBookDTO;
    }
    
    @Override
    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(book, bookDTO);
        return bookDTO;
    }
    
    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(book -> {
                    BookDTO bookDTO = new BookDTO();
                    BeanUtils.copyProperties(book, bookDTO);
                    return bookDTO;
                })
                .collect(Collectors.toList());
    }
    
    @Override
    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        // Don't update id or dateAdded
        bookDTO.setId(existingBook.getId());
        bookDTO.setDateAdded(existingBook.getDateAdded());
        
        BeanUtils.copyProperties(bookDTO, existingBook);
        Book updatedBook = bookRepository.save(existingBook);
        
        BookDTO updatedBookDTO = new BookDTO();
        BeanUtils.copyProperties(updatedBook, updatedBookDTO);
        return updatedBookDTO;
    }
    
    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        
        bookRepository.deleteById(id);
    }
}