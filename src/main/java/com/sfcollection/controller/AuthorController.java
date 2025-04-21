package com.sfcollection.controller;

import com.sfcollection.dto.AuthorDTO;
import com.sfcollection.service.AuthorService;
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
@RequestMapping("/api/v1/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Author management APIs")
public class AuthorController {

    private final AuthorService authorService;
    
    @PostMapping
    @Operation(summary = "Create a new author", description = "Creates a new author in the system")
    @ApiResponse(responseCode = "201", description = "Author created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    public ResponseEntity<Map<String, Object>> createAuthor(@Valid @RequestBody AuthorDTO authorDTO) {
        AuthorDTO createdAuthor = authorService.createAuthor(authorDTO);
        Map<String, Object> response = Map.of(
            "data", createdAuthor,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieves an author by their ID")
    @ApiResponse(responseCode = "200", description = "Author found")
    @ApiResponse(responseCode = "404", description = "Author not found")
    public ResponseEntity<Map<String, Object>> getAuthorById(@PathVariable Long id) {
        AuthorDTO author = authorService.getAuthorById(id);
        Map<String, Object> response = Map.of(
            "data", author,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieves all authors in the system")
    @ApiResponse(responseCode = "200", description = "Authors retrieved successfully",
               content = @Content(schema = @Schema(implementation = AuthorDTO.class)))
    public ResponseEntity<Map<String, Object>> getAllAuthors() {
        List<AuthorDTO> authors = authorService.getAllAuthors();
        Map<String, Object> response = Map.of(
            "data", authors,
            "meta", Map.of(
                "timestamp", LocalDateTime.now(),
                "total", authors.size()
            )
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search authors by name", description = "Searches for authors whose names contain the search string")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public ResponseEntity<Map<String, Object>> searchAuthorsByName(@RequestParam String name) {
        List<AuthorDTO> authors = authorService.searchAuthorsByName(name);
        Map<String, Object> response = Map.of(
            "data", authors,
            "meta", Map.of(
                "timestamp", LocalDateTime.now(),
                "total", authors.size(),
                "search", name
            )
        );
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an author", description = "Updates an existing author")
    @ApiResponse(responseCode = "200", description = "Author updated successfully")
    @ApiResponse(responseCode = "404", description = "Author not found")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    public ResponseEntity<Map<String, Object>> updateAuthor(
            @PathVariable Long id, 
            @Valid @RequestBody AuthorDTO authorDTO) {
        
        AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
        Map<String, Object> response = Map.of(
            "data", updatedAuthor,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author", description = "Deletes an author from the system")
    @ApiResponse(responseCode = "204", description = "Author deleted successfully")
    @ApiResponse(responseCode = "404", description = "Author not found")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{authorId}/books/{bookId}")
    @Operation(summary = "Add book to author", description = "Adds a book to an author's list of books")
    @ApiResponse(responseCode = "200", description = "Book added to author successfully")
    @ApiResponse(responseCode = "404", description = "Author or book not found")
    public ResponseEntity<Map<String, Object>> addBookToAuthor(
            @PathVariable Long authorId, 
            @PathVariable Long bookId) {
        
        AuthorDTO updatedAuthor = authorService.addBookToAuthor(authorId, bookId);
        Map<String, Object> response = Map.of(
            "data", updatedAuthor,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{authorId}/books/{bookId}")
    @Operation(summary = "Remove book from author", description = "Removes a book from an author's list of books")
    @ApiResponse(responseCode = "200", description = "Book removed from author successfully")
    @ApiResponse(responseCode = "404", description = "Author or book not found")
    public ResponseEntity<Map<String, Object>> removeBookFromAuthor(
            @PathVariable Long authorId, 
            @PathVariable Long bookId) {
        
        AuthorDTO updatedAuthor = authorService.removeBookFromAuthor(authorId, bookId);
        Map<String, Object> response = Map.of(
            "data", updatedAuthor,
            "meta", Map.of("timestamp", LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
}