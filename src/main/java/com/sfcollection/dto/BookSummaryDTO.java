package com.sfcollection.dto;

import com.sfcollection.model.SubGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSummaryDTO {
    private Long id;
    private String title;
    private String isbn;
    private SubGenre subGenre;
    private String coverImage;
}