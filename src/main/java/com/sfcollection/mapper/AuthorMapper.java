package com.sfcollection.mapper;

import com.sfcollection.dto.AuthorDTO;
import com.sfcollection.dto.AuthorSummaryDTO;
import com.sfcollection.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface AuthorMapper {
    
    @Mapping(target = "books", source = "books")
    AuthorDTO toDto(Author author);
    
    @Mapping(target = "books", ignore = true)
    Author toEntity(AuthorDTO authorDTO);
    
    List<AuthorDTO> toDtoList(List<Author> authors);
    
    AuthorSummaryDTO toSummaryDto(Author author);
    
    Set<AuthorSummaryDTO> toSummaryDtoSet(Set<Author> authors);
}