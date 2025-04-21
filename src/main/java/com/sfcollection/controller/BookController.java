package com.sfcollection.controller;

import com.sfcollection.dto.*;
import com.sfcollection.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ResponseDTO<BookDTO>> createBook(
            @Valid @RequestBody BookCreateDTO bookCreateDTO) {
        BookDTO createdBook = bookService.createBook(bookCreateDTO);
        return new ResponseEntity<>(ResponseDTO.of(createdBook), HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Retrieves a book by its ID")
    @ApiResponse(responseCode = "200", description = "Book found")
    @ApiResponse(responseCode = "404", description = "Book not found")
    public ResponseEntity<ResponseDTO<BookDTO>> getBookById(
            @Parameter(description = "ID of the book to retrieve", required = true)
            @PathVariable Long id) {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(ResponseDTO.of(book));
    }
    
    @GetMapping
    @Operation(summary = "Get all books", description = "Retrieves all books with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully",
               content = @Content(schema = @Schema(implementation = BookDTO.class)))
    public ResponseEntity<PageResponseDTO<BookDTO>> getAllBooks(
            @ParameterObject @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        Page<BookDTO> books = bookService.getAllBooks(pageable);
        return ResponseEntity.ok(PageResponseDTO.from(books));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search books", description = "Search books by various criteria with pagination and sorting")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    public ResponseEntity<PageResponseDTO<BookDTO>> searchBooks(
            @ParameterObject BookSearchDTO searchDTO,
            @ParameterObject @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        Page<BookDTO> books = bookService.searchBooks(searchDTO, pageable);
        return ResponseEntity.ok(PageResponseDTO.from(books));
    }
    
    @GetMapping("/authors/{authorId}")
    @Operation(summary = "Get books by author", description = "Retrieves all books by a specific author")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    @ApiResponse(responseCode = "404", description = "Author not found")
    public ResponseEntity<PageResponseDTO<BookDTO>> getBooksByAuthor(
            @PathVariable Long authorId,
            @ParameterObject @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        Page<BookDTO> books = bookService.getBooksByAuthor(authorId, pageable);
        return ResponseEntity.ok(PageResponseDTO.from(books));
    }
    
    @GetMapping("/collections/{collectionId}")
    @Operation(summary = "Get books by collection", description = "Retrieves all books in a specific collection")
    @ApiResponse(responseCode = "200", description = "Books retrieved successfully")
    public ResponseEntity<PageResponseDTO<BookDTO>> getBooksByCollection(
            @PathVariable Long collectionId,
            @ParameterObject @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        Page<BookDTO> books = bookService.getBooksByCollection(collectionId, pageable);
        return ResponseEntity.ok(PageResponseDTO.from(books));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a book", description = "Fully updates an existing book")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    public ResponseEntity<ResponseDTO<BookDTO>> updateBook(
            @PathVariable Long id, 
            @Valid @RequestBody BookUpdateDTO bookUpdateDTO) {
        
        BookDTO updatedBook = bookService.updateBook(id, bookUpdateDTO);
        return ResponseEntity.ok(ResponseDTO.of(updatedBook));
    }
    
    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a book", description = "Partially updates an existing book")
    @ApiResponse(responseCode = "200", description = "Book updated successfully")
    @ApiResponse(responseCode = "404", description = "Book not found")
    @ApiResponse(responseCode = "400", description = "Bad request - invalid input")
    public ResponseEntity<ResponseDTO<BookDTO>> patchBook(
            @PathVariable Long id, 
            @Valid @RequestBody BookPatchDTO bookPatchDTO) {
        
        BookDTO updatedBook = bookService.patchBook(id, bookPatchDTO);
        return ResponseEntity.ok(ResponseDTO.of(updatedBook));
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
    public ResponseEntity<ResponseDTO<BookDTO>> addAuthorToBook(
            @PathVariable Long bookId, 
            @PathVariable Long authorId) {
        
        BookDTO updatedBook = bookService.addAuthorToBook(bookId, authorId);
        return ResponseEntity.ok(ResponseDTO.of(updatedBook));
    }
    
    @DeleteMapping("/{bookId}/authors/{authorId}")
    @Operation(summary = "Remove author from book", description = "Removes an author from a book")
    @ApiResponse(responseCode = "200", description = "Author removed from book successfully")
    @ApiResponse(responseCode = "404", description = "Book or author not found")
    public ResponseEntity<ResponseDTO<BookDTO>> removeAuthorFromBook(
            @PathVariable Long bookId, 
            @PathVariable Long authorId) {
        
        BookDTO updatedBook = bookService.removeAuthorFromBook(bookId, authorId);
        return ResponseEntity.ok(ResponseDTO.of(updatedBook));
    }
}