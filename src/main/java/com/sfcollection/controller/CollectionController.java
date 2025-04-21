package com.sfcollection.controller;

import com.sfcollection.dto.CollectionDTO;
import com.sfcollection.service.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/collections")
@RequiredArgsConstructor
@Tag(name = "Collections", description = "Collection management APIs")
public class CollectionController {

    private final CollectionService collectionService;
    
    @PostMapping
    @Operation(summary = "Create a new collection", description = "Creates a new collection in the system")
    @ApiResponse(responseCode = "201", description = "Collection created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    public ResponseEntity<Map<String, Object>> createCollection(@Valid @RequestBody CollectionDTO collectionDTO) {
        CollectionDTO createdCollection = collectionService.createCollection(collectionDTO);
        Map<String, Object> response = Map.of(
            "data", createdCollection,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get collection by ID", description = "Retrieves a collection by its ID")
    @ApiResponse(responseCode = "200", description = "Collection found")
    @ApiResponse(responseCode = "404", description = "Collection not found")
    public ResponseEntity<Map<String, Object>> getCollectionById(@PathVariable Long id) {
        CollectionDTO collection = collectionService.getCollectionById(id);
        Map<String, Object> response = Map.of(
            "data", collection,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all collections", description = "Retrieves all collections in the system")
    @ApiResponse(responseCode = "200", description = "Collections retrieved successfully",
               content = @Content(schema = @Schema(implementation = CollectionDTO.class)))
    public ResponseEntity<Map<String, Object>> getAllCollections() {
        List<CollectionDTO> collections = collectionService.getAllCollections();
        Map<String, Object> response = Map.of(
            "data", collections,
            "meta", Map.of(
                "timestamp", LocalDateTime.now(),
                "total", collections.size()
            )
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search collections by name", description = "Searches for collections whose names contain the search string")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public ResponseEntity<Map<String, Object>> searchCollectionsByName(@RequestParam String name) {
        List<CollectionDTO> collections = collectionService.searchCollectionsByName(name);
        Map<String, Object> response = Map.of(
            "data", collections,
            "meta", Map.of(
                "timestamp", LocalDateTime.now(),
                "total", collections.size(),
                "search", name
            )
        );
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a collection", description = "Updates an existing collection")
    @ApiResponse(responseCode = "200", description = "Collection updated successfully")
    @ApiResponse(responseCode = "404", description = "Collection not found")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    public ResponseEntity<Map<String, Object>> updateCollection(
            @PathVariable Long id, 
            @Valid @RequestBody CollectionDTO collectionDTO) {
        
        CollectionDTO updatedCollection = collectionService.updateCollection(id, collectionDTO);
        Map<String, Object> response = Map.of(
            "data", updatedCollection,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a collection", description = "Deletes a collection from the system")
    @ApiResponse(responseCode = "204", description = "Collection deleted successfully")
    @ApiResponse(responseCode = "404", description = "Collection not found")
    public ResponseEntity<Void> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{collectionId}/books/{bookId}")
    @Operation(summary = "Add book to collection", description = "Adds a book to a collection")
    @ApiResponse(responseCode = "200", description = "Book added to collection successfully")
    @ApiResponse(responseCode = "404", description = "Collection or book not found")
    public ResponseEntity<Map<String, Object>> addBookToCollection(
            @PathVariable Long collectionId, 
            @PathVariable Long bookId) {
        
        CollectionDTO updatedCollection = collectionService.addBookToCollection(collectionId, bookId);
        Map<String, Object> response = Map.of(
            "data", updatedCollection,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{collectionId}/books")
    @Operation(summary = "Add multiple books to collection", description = "Adds multiple books to a collection")
    @ApiResponse(responseCode = "200", description = "Books added to collection successfully")
    @ApiResponse(responseCode = "404", description = "Collection or one of the books not found")
    public ResponseEntity<Map<String, Object>> addBooksToCollection(
            @PathVariable Long collectionId,
            @RequestBody List<Long> bookIds) {
        
        CollectionDTO updatedCollection = collectionService.addBooksToCollection(collectionId, bookIds);
        Map<String, Object> response = Map.of(
            "data", updatedCollection,
            "meta", Map.of(
                "timestamp", LocalDateTime.now(),
                "addedBooks", bookIds.size()
            )
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{collectionId}/books/{bookId}")
    @Operation(summary = "Remove book from collection", description = "Removes a book from a collection")
    @ApiResponse(responseCode = "200", description = "Book removed from collection successfully")
    @ApiResponse(responseCode = "404", description = "Collection or book not found")
    public ResponseEntity<Map<String, Object>> removeBookFromCollection(
            @PathVariable Long collectionId, 
            @PathVariable Long bookId) {
        
        CollectionDTO updatedCollection = collectionService.removeBookFromCollection(collectionId, bookId);
        Map<String, Object> response = Map.of(
            "data", updatedCollection,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
}