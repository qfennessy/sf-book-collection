package com.sfcollection.service;

import com.sfcollection.dto.BookDTO;
import com.sfcollection.exception.ResourceNotFoundException;
import com.sfcollection.mapper.BookMapper;
import com.sfcollection.model.Author;
import com.sfcollection.model.Book;
import com.sfcollection.model.ReadStatus;
import com.sfcollection.model.SubGenre;
import com.sfcollection.repository.AuthorRepository;
import com.sfcollection.repository.BookRepository;
import com.sfcollection.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    
    @Mock
    private AuthorRepository authorRepository;
    
    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book testBook;
    private BookDTO testBookDTO;
    private Author testAuthor;

    @BeforeEach
    void setUp() {
        // Set up test data
        testAuthor = Author.builder()
                .id(1L)
                .name("Frank Herbert")
                .biography("American science fiction author")
                .books(new HashSet<>())
                .build();
                
        testBook = Book.builder()
                .id(1L)
                .title("Dune")
                .isbn("9780441172719")
                .publishedDate(LocalDate.of(1965, 8, 1))
                .description("Science fiction novel by Frank Herbert")
                .coverImage("dune-cover.jpg")
                .subGenre(SubGenre.SPACE_OPERA)
                .pageCount(412)
                .publisher("Chilton Books")
                .language("English")
                .rating(4.5f)
                .readStatus(ReadStatus.COMPLETED)
                .dateAdded(LocalDateTime.now())
                .authors(new HashSet<>())
                .build();

        testBookDTO = BookDTO.builder()
                .id(1L)
                .title("Dune")
                .isbn("9780441172719")
                .publishedDate(LocalDate.of(1965, 8, 1))
                .description("Science fiction novel by Frank Herbert")
                .coverImage("dune-cover.jpg")
                .subGenre(SubGenre.SPACE_OPERA)
                .pageCount(412)
                .publisher("Chilton Books")
                .language("English")
                .rating(4.5f)
                .readStatus(ReadStatus.COMPLETED)
                .dateAdded(testBook.getDateAdded())
                .authors(new HashSet<>())
                .build();
    }

    @Test
    void createBook_ShouldReturnSavedBookDTO() {
        // Arrange
        when(bookMapper.toEntity(any(BookDTO.class))).thenReturn(testBook);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        when(bookMapper.toDto(any(Book.class))).thenReturn(testBookDTO);

        // Act
        BookDTO result = bookService.createBook(testBookDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO, result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void getBookById_WithExistingId_ShouldReturnBookDTO() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookMapper.toDto(testBook)).thenReturn(testBookDTO);

        // Act
        BookDTO result = bookService.getBookById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO, result);
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(99L);
        });
        verify(bookRepository, times(1)).findById(99L);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() {
        // Arrange
        Book secondBook = Book.builder()
                .id(2L)
                .title("Neuromancer")
                .isbn("9780441569595")
                .subGenre(SubGenre.CYBERPUNK)
                .dateAdded(LocalDateTime.now())
                .authors(new HashSet<>())
                .build();
        
        List<Book> books = Arrays.asList(testBook, secondBook);
        List<BookDTO> bookDTOs = Arrays.asList(testBookDTO, BookDTO.builder().id(2L).title("Neuromancer").build());
        
        when(bookRepository.findAll()).thenReturn(books);
        when(bookMapper.toDtoList(books)).thenReturn(bookDTOs);

        // Act
        List<BookDTO> result = bookService.getAllBooks();

        // Assert
        assertEquals(2, result.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void updateBook_WithExistingId_ShouldReturnUpdatedBookDTO() {
        // Arrange
        BookDTO updateDTO = BookDTO.builder()
                .title("Updated Dune")
                .isbn("9780441172719")
                .build();
        
        Book updatedBook = Book.builder()
                .id(testBook.getId())
                .title("Updated Dune")
                .isbn("9780441172719")
                .dateAdded(testBook.getDateAdded())
                .authors(testBook.getAuthors())
                .build();
        
        BookDTO updatedBookDTO = BookDTO.builder()
                .id(testBook.getId())
                .title("Updated Dune")
                .isbn("9780441172719")
                .dateAdded(testBook.getDateAdded())
                .build();
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookMapper.toEntity(updateDTO)).thenReturn(updatedBook);
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(updatedBookDTO);

        // Act
        BookDTO result = bookService.updateBook(1L, updateDTO);

        // Assert
        assertEquals(updatedBookDTO, result);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void deleteBook_WithExistingId_ShouldDeleteBook() {
        // Arrange
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository, times(1)).existsById(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteBook_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(99L);
        });
        verify(bookRepository, times(1)).existsById(99L);
        verify(bookRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void addAuthorToBook_ShouldReturnUpdatedBookDTO() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(bookRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toDto(testBook)).thenReturn(testBookDTO);

        // Act
        BookDTO result = bookService.addAuthorToBook(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO, result);
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(testBook);
    }
    
    @Test
    void removeAuthorFromBook_ShouldReturnUpdatedBookDTO() {
        // Arrange
        testBook.getAuthors().add(testAuthor);
        testAuthor.getBooks().add(testBook);
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(bookRepository.save(testBook)).thenReturn(testBook);
        when(bookMapper.toDto(testBook)).thenReturn(testBookDTO);

        // Act
        BookDTO result = bookService.removeAuthorFromBook(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO, result);
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(testBook);
    }
}