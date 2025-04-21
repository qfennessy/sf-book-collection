package com.sfcollection.dto;

import jakarta.validation.constraints.NotBlank;
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
public class AuthorDTO {
    private Long id;
    
    @NotBlank(message = "Author name is required")
    private String name;
    
    private String biography;
    private LocalDate birthDate;
    private String photoUrl;
    private Set<BookSummaryDTO> books;
}