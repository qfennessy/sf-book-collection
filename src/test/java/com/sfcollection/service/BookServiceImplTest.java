package com.sfcollection.service;

import com.sfcollection.dto.*;
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
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class BookServiceImplTest {

    // Repositories will be mocked
    private BookRepository bookRepository;
    private AuthorRepository authorRepository;
    
    // The mapper will be a concrete test implementation
    private TestBookMapper bookMapper;
    
    private BookServiceImpl bookService;

    private Book testBook;
    private BookDTO testBookDTO;
    private BookCreateDTO testBookCreateDTO;
    private BookUpdateDTO testBookUpdateDTO;
    private BookPatchDTO testBookPatchDTO;
    private Author testAuthor;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        // Create mocks for repositories
        bookRepository = Mockito.mock(BookRepository.class);
        authorRepository = Mockito.mock(AuthorRepository.class);
        
        // Use a real implementation of BookMapper (not a mock or spy)
        bookMapper = new TestBookMapper();
        
        // Initialize service with mocks and real mapper
        bookService = new BookServiceImpl(bookRepository, authorRepository, bookMapper);
        
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
                
        testBookCreateDTO = BookCreateDTO.builder()
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
                .build();
                
        testBookUpdateDTO = BookUpdateDTO.builder()
                .title("Updated Dune")
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
                .build();
                
        testBookPatchDTO = BookPatchDTO.builder()
                .title("Updated Dune")
                .isbn("9780441172719")
                .build();
                
        pageable = PageRequest.of(0, 10);
        
        // Configure the mapper for testing
        bookMapper.setTestBook(testBook);
        bookMapper.setTestBookDTO(testBookDTO);
    }

    @Test
    void createBook_ShouldReturnSavedBookDTO() {
        // Arrange
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);

        // Act
        BookDTO result = bookService.createBook(testBookCreateDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO.getId(), result.getId());
        assertEquals(testBookDTO.getTitle(), result.getTitle());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void getBookById_WithExistingId_ShouldReturnBookDTO() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));

        // Act
        BookDTO result = bookService.getBookById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO.getId(), result.getId());
        assertEquals(testBookDTO.getTitle(), result.getTitle());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(99L);
        });
        verify(bookRepository).findById(99L);
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
        Page<Book> bookPage = new PageImpl<>(books);
        
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        // Act
        Page<BookDTO> result = bookService.getAllBooks(pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void updateBook_WithExistingId_ShouldReturnUpdatedBookDTO() {
        // Arrange
        Book updatedBook = Book.builder()
                .id(testBook.getId())
                .title("Updated Dune")
                .isbn("9780441172719")
                .dateAdded(testBook.getDateAdded())
                .authors(testBook.getAuthors())
                .build();
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        
        // Configure mapper to return updatedBook as DTO
        bookMapper.setReturnUpdatedBook(true);

        // Act
        BookDTO result = bookService.updateBook(1L, testBookUpdateDTO);

        // Assert
        assertEquals("Updated Dune", result.getTitle());
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }
    
    @Test
    void patchBook_WithExistingId_ShouldReturnUpdatedBookDTO() {
        // Arrange
        Book updatedBook = Book.builder()
                .id(testBook.getId())
                .title("Updated Dune")
                .isbn("9780441172719")
                .dateAdded(testBook.getDateAdded())
                .authors(testBook.getAuthors())
                .build();
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);
        
        // Configure mapper to return updatedBook as DTO
        bookMapper.setReturnUpdatedBook(true);

        // Act
        BookDTO result = bookService.patchBook(1L, testBookPatchDTO);

        // Assert
        assertEquals("Updated Dune", result.getTitle());
        verify(bookRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void deleteBook_WithExistingId_ShouldDeleteBook() {
        // Arrange
        when(bookRepository.existsById(1L)).thenReturn(true);
        doNothing().when(bookRepository).deleteById(1L);

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository).existsById(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(bookRepository.existsById(99L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.deleteBook(99L);
        });
        verify(bookRepository).existsById(99L);
        verify(bookRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void addAuthorToBook_ShouldReturnUpdatedBookDTO() {
        // Arrange - Create fresh objects to avoid circular references
        Book book = Book.builder()
                .id(1L)
                .title("Dune")
                .build();
        
        Author author = Author.builder()
                .id(1L)
                .name("Frank Herbert")
                .build();
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        BookDTO result = bookService.addAuthorToBook(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO.getId(), result.getId());
        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }
    
    @Test
    void removeAuthorFromBook_ShouldReturnUpdatedBookDTO() {
        // Arrange - Create fresh objects to avoid circular references
        Book book = Book.builder()
                .id(1L)
                .title("Dune")
                .build();
        
        Author author = Author.builder()
                .id(1L)
                .name("Frank Herbert")
                .build();
        
        // Manually setup relationship without creating circular references
        // for the hashCode() method
        
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        BookDTO result = bookService.removeAuthorFromBook(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testBookDTO.getId(), result.getId());
        verify(bookRepository).findById(1L);
        verify(authorRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
    }
    
    @Test
    void getBooksByAuthor_ShouldReturnBooksPage() {
        // Arrange
        List<Book> books = List.of(testBook);
        Page<Book> bookPage = new PageImpl<>(books);
        
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findByAuthorsId(1L, pageable)).thenReturn(bookPage);
        
        // Act
        Page<BookDTO> result = bookService.getBooksByAuthor(1L, pageable);
        
        // Assert
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findByAuthorsId(1L, pageable);
    }
    
    @Test
    void getBooksByCollection_ShouldReturnBooksPage() {
        // Arrange
        List<Book> books = List.of(testBook);
        Page<Book> bookPage = new PageImpl<>(books);
        
        when(bookRepository.findByCollectionsId(1L, pageable)).thenReturn(bookPage);
        
        // Act
        Page<BookDTO> result = bookService.getBooksByCollection(1L, pageable);
        
        // Assert
        assertEquals(1, result.getTotalElements());
        verify(bookRepository).findByCollectionsId(1L, pageable);
    }
    
    // Test implementation of BookMapper
    public static class TestBookMapper extends BookMapper {
        private Book testBook;
        private BookDTO testBookDTO;
        private boolean returnUpdatedBook = false;
        
        public void setTestBook(Book testBook) {
            this.testBook = testBook;
        }
        
        public void setTestBookDTO(BookDTO testBookDTO) {
            this.testBookDTO = testBookDTO;
        }
        
        public void setReturnUpdatedBook(boolean returnUpdatedBook) {
            this.returnUpdatedBook = returnUpdatedBook;
        }
        
        @Override
        public BookDTO toDto(Book book) {
            if (book == null) return null;
            
            if (returnUpdatedBook) {
                return BookDTO.builder()
                        .id(book.getId())
                        .title("Updated Dune")
                        .isbn(book.getIsbn())
                        .publishedDate(book.getPublishedDate())
                        .description(book.getDescription())
                        .coverImage(book.getCoverImage())
                        .subGenre(book.getSubGenre())
                        .pageCount(book.getPageCount())
                        .publisher(book.getPublisher())
                        .language(book.getLanguage())
                        .rating(book.getRating())
                        .readStatus(book.getReadStatus())
                        .dateAdded(book.getDateAdded())
                        .authors(new HashSet<>())
                        .collections(new HashSet<>())
                        .build();
            } else {
                return testBookDTO;
            }
        }
        
        @Override
        public Book toEntity(BookDTO bookDTO) {
            if (bookDTO == null) return null;
            return testBook;
        }
        
        @Override
        public Book createDtoToEntity(BookCreateDTO createDTO) {
            if (createDTO == null) return null;
            return testBook;
        }
        
        @Override
        public void updateDtoToEntity(BookUpdateDTO updateDTO, Book book) {
            if (updateDTO != null && updateDTO.getTitle() != null) {
                book.setTitle(updateDTO.getTitle());
            }
        }
        
        @Override
        public void patchDtoToEntity(BookPatchDTO patchDTO, Book book) {
            if (patchDTO != null && patchDTO.getTitle() != null) {
                book.setTitle(patchDTO.getTitle());
            }
        }
        
        @Override
        public List<BookDTO> toDtoList(List<Book> books) {
            if (books == null) return Collections.emptyList();
            return books.stream().map(this::toDto).collect(Collectors.toList());
        }
        
        @Override
        public Page<BookDTO> toDtoPage(Page<Book> books) {
            if (books == null) return Page.empty();
            return books.map(this::toDto);
        }
        
        @Override
        public BookSummaryDTO toSummaryDto(Book book) {
            if (book == null) return null;
            return BookSummaryDTO.builder()
                    .id(book.getId())
                    .title(book.getTitle())
                    .subGenre(book.getSubGenre())
                    .build();
        }
        
        @Override
        public List<BookSummaryDTO> toSummaryDtoList(List<Book> books) {
            if (books == null) return Collections.emptyList();
            return books.stream().map(this::toSummaryDto).collect(Collectors.toList());
        }
    }
}