package com.sfcollection.mapper;

import com.sfcollection.dto.BookDTO;
import com.sfcollection.dto.BookSummaryDTO;
import com.sfcollection.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AuthorMapper.class, CollectionMapper.class})
public interface BookMapper {
    
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "collections", source = "collections")
    BookDTO toDto(Book book);
    
    @Mapping(target = "authors", ignore = true)
    Book toEntity(BookDTO bookDTO);
    
    List<BookDTO> toDtoList(List<Book> books);
    
    BookSummaryDTO toSummaryDto(Book book);
    
    List<BookSummaryDTO> toSummaryDtoList(List<Book> books);
}