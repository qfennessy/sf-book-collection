package com.sfcollection.service;

import com.sfcollection.dto.CollectionDTO;
import com.sfcollection.exception.ResourceNotFoundException;
import com.sfcollection.mapper.CollectionMapper;
import com.sfcollection.model.Book;
import com.sfcollection.model.Collection;
import com.sfcollection.repository.BookRepository;
import com.sfcollection.repository.CollectionRepository;
import com.sfcollection.service.impl.CollectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectionServiceImplTest {

    @Mock
    private CollectionRepository collectionRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CollectionMapper collectionMapper;

    @InjectMocks
    private CollectionServiceImpl collectionService;

    private Collection testCollection;
    private CollectionDTO testCollectionDTO;
    private Book testBook;

    @BeforeEach
    void setUp() {
        // Set up test data
        testCollection = Collection.builder()
                .id(1L)
                .name("Science Fiction Classics")
                .description("Classic science fiction novels")
                .dateCreated(LocalDateTime.now())
                .lastModified(LocalDateTime.now())
                .books(new HashSet<>())
                .build();

        testCollectionDTO = CollectionDTO.builder()
                .id(1L)
                .name("Science Fiction Classics")
                .description("Classic science fiction novels")
                .dateCreated(testCollection.getDateCreated())
                .lastModified(testCollection.getLastModified())
                .books(Collections.emptySet())
                .build();

        testBook = Book.builder()
                .id(1L)
                .title("Dune")
                .isbn("9780441172719")
                .collections(new HashSet<>())
                .build();
    }

    @Test
    void createCollection_ShouldReturnSavedCollectionDTO() {
        // Arrange
        when(collectionMapper.toEntity(testCollectionDTO)).thenReturn(testCollection);
        when(collectionRepository.save(testCollection)).thenReturn(testCollection);
        when(collectionMapper.toDto(testCollection)).thenReturn(testCollectionDTO);

        // Act
        CollectionDTO result = collectionService.createCollection(testCollectionDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testCollectionDTO, result);
        verify(collectionRepository, times(1)).save(testCollection);
    }

    @Test
    void getCollectionById_WithExistingId_ShouldReturnCollectionDTO() {
        // Arrange
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(testCollection));
        when(collectionMapper.toDto(testCollection)).thenReturn(testCollectionDTO);

        // Act
        CollectionDTO result = collectionService.getCollectionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCollectionDTO, result);
        verify(collectionRepository, times(1)).findById(1L);
    }

    @Test
    void getCollectionById_WithNonExistingId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(collectionRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            collectionService.getCollectionById(99L);
        });
        verify(collectionRepository, times(1)).findById(99L);
    }

    @Test
    void getAllCollections_ShouldReturnAllCollections() {
        // Arrange
        Collection secondCollection = Collection.builder()
                .id(2L)
                .name("New Releases")
                .books(new HashSet<>())
                .build();
        
        CollectionDTO secondCollectionDTO = CollectionDTO.builder()
                .id(2L)
                .name("New Releases")
                .books(Collections.emptySet())
                .build();
        
        List<Collection> collections = Arrays.asList(testCollection, secondCollection);
        List<CollectionDTO> collectionDTOs = Arrays.asList(testCollectionDTO, secondCollectionDTO);
        
        when(collectionRepository.findAll()).thenReturn(collections);
        when(collectionMapper.toDtoList(collections)).thenReturn(collectionDTOs);

        // Act
        List<CollectionDTO> result = collectionService.getAllCollections();

        // Assert
        assertEquals(2, result.size());
        verify(collectionRepository, times(1)).findAll();
    }

    @Test
    void searchCollectionsByName_ShouldReturnMatchingCollections() {
        // Arrange
        List<Collection> collections = Collections.singletonList(testCollection);
        List<CollectionDTO> collectionDTOs = Collections.singletonList(testCollectionDTO);
        
        when(collectionRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(collections);
        when(collectionMapper.toDtoList(collections)).thenReturn(collectionDTOs);

        // Act
        List<CollectionDTO> result = collectionService.searchCollectionsByName("Classics");

        // Assert
        assertEquals(1, result.size());
        verify(collectionRepository, times(1)).findByNameContainingIgnoreCase("Classics");
    }

    @Test
    void updateCollection_WithExistingId_ShouldReturnUpdatedCollectionDTO() {
        // Arrange
        CollectionDTO updateDTO = CollectionDTO.builder()
                .name("Updated Classics")
                .description("Updated description")
                .build();
        
        Collection updatedCollection = Collection.builder()
                .id(1L)
                .name("Updated Classics")
                .description("Updated description")
                .dateCreated(testCollection.getDateCreated())
                .lastModified(LocalDateTime.now())
                .books(testCollection.getBooks())
                .build();
        
        CollectionDTO updatedCollectionDTO = CollectionDTO.builder()
                .id(1L)
                .name("Updated Classics")
                .description("Updated description")
                .dateCreated(testCollection.getDateCreated())
                .lastModified(updatedCollection.getLastModified())
                .books(Collections.emptySet())
                .build();
        
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(testCollection));
        when(collectionRepository.save(any(Collection.class))).thenReturn(updatedCollection);
        when(collectionMapper.toDto(updatedCollection)).thenReturn(updatedCollectionDTO);

        // Act
        CollectionDTO result = collectionService.updateCollection(1L, updateDTO);

        // Assert
        assertEquals("Updated Classics", result.getName());
        assertEquals("Updated description", result.getDescription());
        verify(collectionRepository, times(1)).findById(1L);
        verify(collectionRepository, times(1)).save(any(Collection.class));
    }

    @Test
    void deleteCollection_WithExistingId_ShouldDeleteCollection() {
        // Arrange
        when(collectionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(collectionRepository).deleteById(1L);

        // Act
        collectionService.deleteCollection(1L);

        // Assert
        verify(collectionRepository, times(1)).existsById(1L);
        verify(collectionRepository, times(1)).deleteById(1L);
    }

    @Test
    void addBookToCollection_ShouldReturnUpdatedCollectionDTO() {
        // Arrange
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(testCollection));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(collectionRepository.save(testCollection)).thenReturn(testCollection);
        when(collectionMapper.toDto(testCollection)).thenReturn(testCollectionDTO);

        // Act
        CollectionDTO result = collectionService.addBookToCollection(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCollectionDTO, result);
        verify(collectionRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(collectionRepository, times(1)).save(testCollection);
    }

    @Test
    void addBooksToCollection_ShouldReturnUpdatedCollectionDTO() {
        // Arrange
        List<Long> bookIds = Collections.singletonList(1L);
        
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(testCollection));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(collectionRepository.save(testCollection)).thenReturn(testCollection);
        when(collectionMapper.toDto(testCollection)).thenReturn(testCollectionDTO);

        // Act
        CollectionDTO result = collectionService.addBooksToCollection(1L, bookIds);

        // Assert
        assertNotNull(result);
        assertEquals(testCollectionDTO, result);
        verify(collectionRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(collectionRepository, times(1)).save(testCollection);
    }

    @Test
    void removeBookFromCollection_ShouldReturnUpdatedCollectionDTO() {
        // Arrange - Create fresh objects to avoid circular references
        Collection collection = Collection.builder()
                .id(1L)
                .name("Classics")
                .books(new HashSet<>())
                .build();
                
        Book book = Book.builder()
                .id(1L)
                .title("Dune")
                .collections(new HashSet<>())
                .build();
        
        // Don't create circular references in the test objects
        
        when(collectionRepository.findById(1L)).thenReturn(Optional.of(collection));
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(collectionRepository.save(any(Collection.class))).thenReturn(collection);
        when(collectionMapper.toDto(any(Collection.class))).thenReturn(testCollectionDTO);

        // Act
        CollectionDTO result = collectionService.removeBookFromCollection(1L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCollectionDTO, result);
        verify(collectionRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).findById(1L);
        verify(collectionRepository, times(1)).save(any(Collection.class));
    }
}