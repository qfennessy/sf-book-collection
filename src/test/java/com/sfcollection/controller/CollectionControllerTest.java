package com.sfcollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sfcollection.dto.CollectionDTO;
import com.sfcollection.service.CollectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
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

@WebMvcTest(CollectionController.class)
class CollectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CollectionService collectionService;

    private CollectionDTO testCollectionDTO;

    @BeforeEach
    void setUp() {
        testCollectionDTO = CollectionDTO.builder()
                .id(1L)
                .name("Science Fiction Classics")
                .description("Classic science fiction novels")
                .dateCreated(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .books(Collections.emptySet())
                .build();
    }

    @Test
    void createCollection_ShouldReturnCreatedCollection() throws Exception {
        // Arrange
        when(collectionService.createCollection(any(CollectionDTO.class))).thenReturn(testCollectionDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/collections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCollectionDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Science Fiction Classics")));

        verify(collectionService, times(1)).createCollection(any(CollectionDTO.class));
    }

    @Test
    void getCollectionById_ShouldReturnCollection() throws Exception {
        // Arrange
        when(collectionService.getCollectionById(1L)).thenReturn(testCollectionDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/collections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Science Fiction Classics")));

        verify(collectionService, times(1)).getCollectionById(1L);
    }

    @Test
    void getAllCollections_ShouldReturnAllCollections() throws Exception {
        // Arrange
        CollectionDTO secondCollection = CollectionDTO.builder()
                .id(2L)
                .name("New Releases")
                .description("Recently published science fiction")
                .build();
        
        List<CollectionDTO> collections = Arrays.asList(testCollectionDTO, secondCollection);
        when(collectionService.getAllCollections()).thenReturn(collections);

        // Act & Assert
        mockMvc.perform(get("/api/v1/collections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Science Fiction Classics")))
                .andExpect(jsonPath("$.data[1].id", is(2)))
                .andExpect(jsonPath("$.data[1].name", is("New Releases")));

        verify(collectionService, times(1)).getAllCollections();
    }

    @Test
    void searchCollectionsByName_ShouldReturnMatchingCollections() throws Exception {
        // Arrange
        List<CollectionDTO> collections = Collections.singletonList(testCollectionDTO);
        when(collectionService.searchCollectionsByName(anyString())).thenReturn(collections);

        // Act & Assert
        mockMvc.perform(get("/api/v1/collections/search?name=Classics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].name", is("Science Fiction Classics")));

        verify(collectionService, times(1)).searchCollectionsByName("Classics");
    }

    @Test
    void updateCollection_ShouldReturnUpdatedCollection() throws Exception {
        // Arrange
        CollectionDTO updatedCollection = CollectionDTO.builder()
                .id(1L)
                .name("Updated Classics")
                .description("Updated description")
                .build();
        
        when(collectionService.updateCollection(eq(1L), any(CollectionDTO.class))).thenReturn(updatedCollection);

        // Act & Assert
        mockMvc.perform(put("/api/v1/collections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCollection)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Updated Classics")));

        verify(collectionService, times(1)).updateCollection(eq(1L), any(CollectionDTO.class));
    }

    @Test
    void deleteCollection_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(collectionService).deleteCollection(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/collections/1"))
                .andExpect(status().isNoContent());

        verify(collectionService, times(1)).deleteCollection(1L);
    }

    @Test
    void addBookToCollection_ShouldReturnUpdatedCollection() throws Exception {
        // Arrange
        when(collectionService.addBookToCollection(anyLong(), anyLong())).thenReturn(testCollectionDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/collections/1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Science Fiction Classics")));

        verify(collectionService, times(1)).addBookToCollection(1L, 1L);
    }

    @Test
    void addBooksToCollection_ShouldReturnUpdatedCollection() throws Exception {
        // Arrange
        List<Long> bookIds = Arrays.asList(1L, 2L, 3L);
        when(collectionService.addBooksToCollection(anyLong(), anyList())).thenReturn(testCollectionDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/collections/1/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Science Fiction Classics")));

        verify(collectionService, times(1)).addBooksToCollection(eq(1L), anyList());
    }

    @Test
    void removeBookFromCollection_ShouldReturnUpdatedCollection() throws Exception {
        // Arrange
        when(collectionService.removeBookFromCollection(anyLong(), anyLong())).thenReturn(testCollectionDTO);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/collections/1/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.name", is("Science Fiction Classics")));

        verify(collectionService, times(1)).removeBookFromCollection(1L, 1L);
    }
}