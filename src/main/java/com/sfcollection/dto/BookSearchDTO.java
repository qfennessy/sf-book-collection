package com.sfcollection.dto;

import com.sfcollection.model.SubGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchDTO {
    private String title;
    private String isbn;
    private SubGenre subGenre;
    private LocalDate publishedAfter;
    private LocalDate publishedBefore;
    private Long authorId;
    private Long collectionId;
}