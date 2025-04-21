package com.sfcollection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionSummaryDTO {
    private Long id;
    private String name;
    private String description;
    private int bookCount;
    private LocalDateTime lastModified;
}