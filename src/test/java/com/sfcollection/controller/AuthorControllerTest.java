package com.sfcollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfcollection.dto.AuthorDTO;
import com.sfcollection.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    private AuthorDTO testAuthorDTO;

    @BeforeEach
    void setUp() {
        testAuthorDTO = AuthorDTO.builder()
                .id(1L)
                .name("Frank Herbert")
                .biography("American science fiction author")
                .birthDate(LocalDate.of(1920, 10, 8))
                .photoUrl("frank_herbert.jpg")
                .books(Collections.emptySet())
                .build();
    }

    @Test
    void createAuthor_ShouldReturnCreatedAuthor() throws Exception {
        // Arrange
        when(authorService.createAuthor(any(AuthorDTO.class))).thenReturn(testAuthorDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testAuthorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Frank Herbert")));

        verify(authorService, times(1)).createAuthor(any(AuthorDTO.class));
    }

    @Test
    void getAuthorById_ShouldReturnAuthor() throws Exception {
        // Arrange
        when(authorService.getAuthorById(1L)).thenReturn(testAuthorDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/authors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Frank Herbert")));

        verify(authorService, times(1)).getAuthorById(1L);
    }

    @Test
    void getAllAuthors_ShouldReturnAllAuthors() throws Exception {
        // Arrange
        AuthorDTO secondAuthor = AuthorDTO.builder()
                .id(2L)
                .name("Isaac Asimov")
                .build();
        
        List<AuthorDTO> authors = Arrays.asList(testAuthorDTO, secondAuthor);
        when(authorService.getAllAuthors()).thenReturn(authors);

        // Act & Assert
        mockMvc.perform(get("/api/v1/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Frank Herbert")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].name", is("Isaac Asimov")));

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void searchAuthorsByName_ShouldReturnMatchingAuthors() throws Exception {
        // Arrange
        List<AuthorDTO> authors = Collections.singletonList(testAuthorDTO);
        when(authorService.searchAuthorsByName(anyString())).thenReturn(authors);

        // Act & Assert
        mockMvc.perform(get("/api/v1/authors/search?name=Frank"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Frank Herbert")));

        verify(authorService, times(1)).searchAuthorsByName("Frank");
    }

    @Test
    void updateAuthor_ShouldReturnUpdatedAuthor() throws Exception {
        // Arrange
        AuthorDTO updatedAuthor = AuthorDTO.builder()
                .id(1L)
                .name("Updated Frank Herbert")
                .biography("Updated biography")
                .birthDate(LocalDate.of(1920, 10, 8))
                .photoUrl("frank_herbert.jpg")
                .books(Collections.emptySet())
                .build();
        
        when(authorService.updateAuthor(eq(1L), any(AuthorDTO.class))).thenReturn(updatedAuthor);

        // Act & Assert
        mockMvc.perform(put("/api/v1/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAuthor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Updated Frank Herbert")));

        verify(authorService, times(1)).updateAuthor(eq(1L), any(AuthorDTO.class));
    }

    @Test
    void deleteAuthor_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(authorService).deleteAuthor(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/authors/1"))
                .andExpect(status().isNoContent());

        verify(authorService, times(1)).deleteAuthor(1L);
    }

    @Test
    void addBookToAuthor_ShouldReturnUpdatedAuthor() throws Exception {
        // Arrange
        when(authorService.addBookToAuthor(anyLong(), anyLong())).thenReturn(testAuthorDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/authors/1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Frank Herbert")));

        verify(authorService, times(1)).addBookToAuthor(1L, 1L);
    }

    @Test
    void removeBookFromAuthor_ShouldReturnUpdatedAuthor() throws Exception {
        // Arrange
        when(authorService.removeBookFromAuthor(anyLong(), anyLong())).thenReturn(testAuthorDTO);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/authors/1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Frank Herbert")));

        verify(authorService, times(1)).removeBookFromAuthor(1L, 1L);
    }
}