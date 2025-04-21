package com.sfcollection.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(unique = true)
    private String isbn;

    private LocalDate publishedDate;

    @Column(length = 4000)
    private String description;

    private String coverImage;

    @Enumerated(EnumType.STRING)
    private SubGenre subGenre;

    private Integer pageCount;

    private String publisher;

    private String language;

    @Min(1)
    @Max(5)
    private Float rating;

    @Enumerated(EnumType.STRING)
    private ReadStatus readStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dateAdded;

    @PrePersist
    protected void onCreate() {
        dateAdded = LocalDateTime.now();
    }
}