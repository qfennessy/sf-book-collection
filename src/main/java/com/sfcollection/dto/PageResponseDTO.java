package com.sfcollection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponseDTO<T> {
    private List<T> data;
    private PageMetaDTO meta;
    
    public static <T> PageResponseDTO<T> from(Page<T> page) {
        return PageResponseDTO.<T>builder()
                .data(page.getContent())
                .meta(PageMetaDTO.builder()
                        .timestamp(LocalDateTime.now())
                        .page(page.getNumber())
                        .size(page.getSize())
                        .totalElements(page.getTotalElements())
                        .totalPages(page.getTotalPages())
                        .last(page.isLast())
                        .first(page.isFirst())
                        .build())
                .build();
    }
    
    public static <T> PageResponseDTO<T> from(Page<T> page, Map<String, Object> additionalMeta) {
        PageMetaDTO meta = PageMetaDTO.builder()
                .timestamp(LocalDateTime.now())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .first(page.isFirst())
                .build();
        
        additionalMeta.forEach(meta::addAdditionalProperty);
        
        return PageResponseDTO.<T>builder()
                .data(page.getContent())
                .meta(meta)
                .build();
    }
}