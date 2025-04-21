package com.sfcollection.repository;

import com.sfcollection.model.Book;
import com.sfcollection.model.SubGenre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    Page<Book> findByAuthorsId(Long authorId, Pageable pageable);
    
    Page<Book> findByCollectionsId(Long collectionId, Pageable pageable);
    
    Page<Book> findBySubGenre(SubGenre subGenre, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE " +
           "(:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:isbn IS NULL OR b.isbn = :isbn) AND " +
           "(:subGenre IS NULL OR b.subGenre = :subGenre) AND " +
           "(:publishedAfter IS NULL OR b.publishedDate >= :publishedAfter) AND " +
           "(:publishedBefore IS NULL OR b.publishedDate <= :publishedBefore)")
    Page<Book> searchBooks(
            @Param("title") String title,
            @Param("isbn") String isbn,
            @Param("subGenre") SubGenre subGenre,
            @Param("publishedAfter") LocalDate publishedAfter,
            @Param("publishedBefore") LocalDate publishedBefore,
            Pageable pageable);
    
    boolean existsByIsbn(String isbn);
}