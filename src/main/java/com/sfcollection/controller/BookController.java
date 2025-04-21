package com.sfcollection.controller;

import com.sfcollection.dto.BookDTO;
import com.sfcollection.service.BookService;
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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "Book management APIs")
public class BookController {

    private final BookService bookService;
    
    @PostMapping
    @Operation(summary = "Create a new book", description = "Creates a new book in the collection")
    @ApiResponse(responseCode = "201", description = "Book created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    public ResponseEntity<Map<String, Object>> createBook(@Valid @RequestBody BookDTO bookDTO) {
        BookDTO createdBook = bookService.createBook(bookDTO);
        Map<String, Object> response = Map.of(
            "data", createdBook,
            "meta", Map.of("timestamp", java.time.LocalDateTime.now())
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its ID")
    @ApiResponse(responseCode = "200", description = "Book found")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<Map<String, Object>> getBookById(@PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        Map<String, Object> response = Map.of(
            "data", book,
            "meta", Map.of("timestamp", java.time.LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves all books in the collection")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
               content = @Content(schema = @Schema(implementation = BookDTO.class)))
    public ResponseEntity<Map<String, Object>> getAllBooks() {
        List<BookDTO> books = bookService.getAllBooks();
        Map<String, Object> response = Map.of(
            "data", books,
            "meta", Map.of(
                "timestamp", java.time.LocalDateTime.now(),
                "total", books.size()
            )
        );
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Updates an existing book")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    public ResponseEntity<Map<String, Object>> updateBook(
            @PathVariable Long id, 
            @Valid @RequestBody BookDTO bookDTO) {
        
        BookDTO updatedBook = bookService.updateBook(id, bookDTO);
        Map<String, Object> response = Map.of(
            "data", updatedBook,
            "meta", Map.of("timestamp", java.time.LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book from the collection")
    @ApiResponse(responseCode = "204", description = "Book deleted successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{bookId}/authors/{authorId}")
    @Operation(summary = "Add author to book", description = "Adds an author to a book")
    @ApiResponse(responseCode = "200", description = "Author added to book successfully")
    @ApiResponse(responseCode = "404", description = "Book or author not found")
    public ResponseEntity<Map<String, Object>> addAuthorToBook(
            @PathVariable Long bookId, 
            @PathVariable Long authorId) {
        
        BookDTO updatedBook = bookService.addAuthorToBook(bookId, authorId);
        Map<String, Object> response = Map.of(
            "data", updatedBook,
            "meta", Map.of("timestamp", java.time.LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{bookId}/authors/{authorId}")
    @Operation(summary = "Remove author from book", description = "Removes an author from a book")
    @ApiResponse(responseCode = "200", description = "Author removed from book successfully")
    @ApiResponse(responseCode = "404", description = "Book or author not found")
    public ResponseEntity<Map<String, Object>> removeAuthorFromBook(
            @PathVariable Long bookId, 
            @PathVariable Long authorId) {
        
        BookDTO updatedBook = bookService.removeAuthorFromBook(bookId, authorId);
        Map<String, Object> response = Map.of(
            "data", updatedBook,
            "meta", Map.of("timestamp", java.time.LocalDateTime.now())
        );
        return ResponseEntity.ok(response);
    }
}