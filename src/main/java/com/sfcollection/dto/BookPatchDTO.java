package com.sfcollection.dto;

import com.sfcollection.model.ReadStatus;
import com.sfcollection.model.SubGenre;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookPatchDTO {
    private String title;
    private String isbn;
    private LocalDate publishedDate;
    private String description;
    private String coverImage;
    private SubGenre subGenre;
    private Integer pageCount;
    private String publisher;
    private String language;
    
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Float rating;
    
    private ReadStatus readStatus;
    private Set<Long> authorIds;
}