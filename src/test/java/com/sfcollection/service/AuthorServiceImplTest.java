package com.sfcollection.service;

import com.sfcollection.dto.AuthorDTO;
import com.sfcollection.exception.ResourceNotFoundException;
import com.sfcollection.mapper.AuthorMapper;
import com.sfcollection.model.Author;
import com.sfcollection.model.Book;
import com.sfcollection.repository.AuthorRepository;
import com.sfcollection.repository.BookRepository;
import com.sfcollection.service.impl.AuthorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author testAuthor;
    private AuthorDTO testAuthorDTO;
    private Book testBook;

    @BeforeEach
    void setUp() {
        // Set up test data
        testAuthor = Author.builder()
                .id(1L)
                .name("Frank Herbert")
                .biography("American science fiction author")
                .birthDate(LocalDate.of(1920, 10, 8))
                .photoUrl("frank_herbert.jpg")
                .books(new HashSet<>())
                .build();

        testAuthorDTO = AuthorDTO.builder()
                .id(1L)
                .name("Frank Herbert")
                .biography("American science fiction author")
                .birthDate(LocalDate.of(1920, 10, 8))
                .photoUrl("frank_herbert.jpg")
                .books(Collections.emptySet())
                .build();

        testBook = Book.builder()
                .id(1L)
                .title("Dune")
                .isbn("9780441172719")
                .authors(new HashSet<>())
                .build();
    }

    @Test
    void createAuthor_ShouldReturnSavedAuthorDTO() {
        // Arrange
        when(authorMapper.toEntity(testAuthorDTO)).thenReturn(testAuthor);
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);
        when(authorMapper.toDto(testAuthor)).thenReturn(testAuthorDTO);

        // Act
        AuthorDTO result = authorService.createAuthor(testAuthorDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testAuthorDTO, result);
        verify(authorRepository, times(1)).save(testAuthor);
    }

    @Test
    void getAuthorById_WithExistingId_ShouldReturnAuthorDTO() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(authorMapper.toDto(testAuthor)).thenReturn(testAuthorDTO);

        // Act
        AuthorDTO result = authorService.getAuthorById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testAuthorDTO, result);
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void getAuthorById_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            authorService.getAuthorById(99L);
        });
        verify(authorRepository, times(1)).findById(99L);
    }

    @Test
    void getAllAuthors_ShouldReturnAllAuthors() {
        // Arrange
        Author secondAuthor = Author.builder()
                .id(2L)
                .name("Isaac Asimov")
                .build();
        
        AuthorDTO secondAuthorDTO = AuthorDTO.builder()
                .id(2L)
                .name("Isaac Asimov")
                .build();
        
        List<Author> authors = Arrays.asList(testAuthor, secondAuthor);
        List<AuthorDTO> authorDTOs = Arrays.asList(testAuthorDTO, secondAuthorDTO);
        
        when(authorRepository.findAll()).thenReturn(authors);
        when(authorMapper.toDtoList(authors)).thenReturn(authorDTOs);

        // Act
        List<AuthorDTO> result = authorService.getAllAuthors();

        // Assert
        assertEquals(2, result.size());
        verify(authorRepository, times(1)).findAll();
    }

    @Test
    void searchAuthorsByName_ShouldReturnMatchingAuthors() {
        // Arrange
        List<Author> authors = Collections.singletonList(testAuthor);
        List<AuthorDTO> authorDTOs = Collections.singletonList(testAuthorDTO);
        
        when(authorRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(authors);
        when(authorMapper.toDtoList(authors)).thenReturn(authorDTOs);

        // Act
        List<AuthorDTO> result = authorService.searchAuthorsByName("Frank");

        // Assert
        assertEquals(1, result.size());
        verify(authorRepository, times(1)).findByNameContainingIgnoreCase("Frank");
    }

    @Test
    void updateAuthor_WithExistingId_ShouldReturnUpdatedAuthorDTO() {
        // Arrange
        AuthorDTO updateDTO = AuthorDTO.builder()
                .name("Updated Frank Herbert")
                .biography("Updated biography")
                .build();
        
        Author updatedAuthor = Author.builder()
                .id(1L)
                .name("Updated Frank Herbert")
                .biography("Updated biography")
                .birthDate(testAuthor.getBirthDate())
                .photoUrl(testAuthor.getPhotoUrl())
                .books(testAuthor.getBooks())
                .build();
        
        AuthorDTO updatedAuthorDTO = AuthorDTO.builder()
                .id(1L)
                .name("Updated Frank Herbert")
                .biography("Updated biography")
                .birthDate(testAuthor.getBirthDate())
                .photoUrl(testAuthor.getPhotoUrl())
                .books(Collections.emptySet())
                .build();
        
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);
        when(authorMapper.toDto(updatedAuthor)).thenReturn(updatedAuthorDTO);

        // Act
        AuthorDTO result = authorService.updateAuthor(1L, updateDTO);

        // Assert
        assertEquals("Updated Frank Herbert", result.getName());
        assertEquals("Updated biography", result.getBiography());
        verify(authorRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void deleteAuthor_WithExistingId_ShouldDeleteAuthor() {
        // Arrange
        when(authorRepository.existsById(1L)).thenReturn(true);
        doNothing().when(authorRepository).deleteById(1L);

        // Act
        authorService.deleteAuthor(1L);

        // Assert
        verify(authorRepository, times(1)).existsById(1L);
        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void addBookToAuthor_ShouldReturnUpdatedAuthorDTO() {
        // Arrange
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);
        when(authorMapper.toDto(testAuthor)).thenReturn(testAuthorDTO);

        // Act
        AuthorDTO result = authorService.addBookToAuthor(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testAuthorDTO, result);
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(testAuthor);
    }

    @Test
    void removeBookFromAuthor_ShouldReturnUpdatedAuthorDTO() {
        // Arrange - Create fresh objects to avoid circular references
        Author author = Author.builder()
                .id(1L)
                .name("Frank Herbert")
                .books(new HashSet<>())
                .build();
                
        Book book = Book.builder()
                .id(1L)
                .title("Dune")
                .authors(new HashSet<>())
                .build();
        
        // Don't create circular references in the test objects
        
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.save(any(Author.class))).thenReturn(author);
        when(authorMapper.toDto(any(Author.class))).thenReturn(testAuthorDTO);

        // Act
        AuthorDTO result = authorService.removeBookFromAuthor(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testAuthorDTO, result);
        verify(authorRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(authorRepository, times(1)).save(any(Author.class));
    }
}