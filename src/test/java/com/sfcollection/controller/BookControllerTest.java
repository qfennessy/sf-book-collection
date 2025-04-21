package com.sfcollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfcollection.dto.BookDTO;
import com.sfcollection.model.ReadStatus;
import com.sfcollection.model.SubGenre;
import com.sfcollection.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    private BookDTO testBookDTO;

    @BeforeEach
    void setUp() {
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
                .dateAdded(LocalDateTime.now())
                .build();
    }

    @Test
    void createBook_ShouldReturnCreatedBook() throws Exception {
        // Arrange
        when(bookService.createBook(any(BookDTO.class))).thenReturn(testBookDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testBookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Dune")))
                .andExpect(jsonPath("$.data.isbn", is("9780441172719")));

        verify(bookService, times(1)).createBook(any(BookDTO.class));
    }

    @Test
    void getBookById_ShouldReturnBook() throws Exception {
        // Arrange
        when(bookService.getBookById(1L)).thenReturn(testBookDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Dune")));

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() throws Exception {
        // Arrange
        BookDTO secondBook = BookDTO.builder()
                .id(2L)
                .title("Neuromancer")
                .isbn("9780441569595")
                .subGenre(SubGenre.CYBERPUNK)
                .dateAdded(LocalDateTime.now())
                .build();
        
        List<BookDTO> books = Arrays.asList(testBookDTO, secondBook);
        when(bookService.getAllBooks()).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/api/v1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].title", is("Dune")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].title", is("Neuromancer")));

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void updateBook_ShouldReturnUpdatedBook() throws Exception {
        // Arrange
        BookDTO updatedBook = BookDTO.builder()
                .id(1L)
                .title("Dune: Deluxe Edition")
                .isbn("9780441172719")
                .build();
        
        when(bookService.updateBook(eq(1L), any(BookDTO.class))).thenReturn(updatedBook);

        // Act & Assert
        mockMvc.perform(put("/api/v1/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Dune: Deluxe Edition")));

        verify(bookService, times(1)).updateBook(eq(1L), any(BookDTO.class));
    }

    @Test
    void deleteBook_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(bookService).deleteBook(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/books/1"))
                .andExpect(status().isNoContent());

        verify(bookService, times(1)).deleteBook(1L);
    }
    
    @Test
    void addAuthorToBook_ShouldReturnUpdatedBook() throws Exception {
        // Arrange
        when(bookService.addAuthorToBook(anyLong(), anyLong())).thenReturn(testBookDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/books/1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Dune")));

        verify(bookService, times(1)).addAuthorToBook(1L, 1L);
    }
    
    @Test
    void removeAuthorFromBook_ShouldReturnUpdatedBook() throws Exception {
        // Arrange
        when(bookService.removeAuthorFromBook(anyLong(), anyLong())).thenReturn(testBookDTO);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/books/1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Dune")));

        verify(bookService, times(1)).removeAuthorFromBook(1L, 1L);
    }
}