package com.sfcollection.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO<T> {
    private T data;
    @Builder.Default
    private Map<String, Object> meta = new HashMap<>();
    
    public static <T> ResponseDTO<T> of(T data) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("timestamp", LocalDateTime.now());
        
        return ResponseDTO.<T>builder()
                .data(data)
                .meta(meta)
                .build();
    }
    
    public static <T> ResponseDTO<T> of(T data, Map<String, Object> additionalMeta) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("timestamp", LocalDateTime.now());
        meta.putAll(additionalMeta);
        
        return ResponseDTO.<T>builder()
                .data(data)
                .meta(meta)
                .build();
    }
}