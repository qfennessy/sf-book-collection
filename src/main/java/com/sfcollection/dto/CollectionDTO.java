package com.sfcollection.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDTO {
    private Long id;
    
    @NotBlank(message = "Collection name is required")
    private String name;
    
    private String description;
    private Set<BookSummaryDTO> books;
    private LocalDateTime dateCreated;
    private LocalDateTime lastModified;
}