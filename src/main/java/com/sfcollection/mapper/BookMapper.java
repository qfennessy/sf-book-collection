package com.sfcollection.mapper;

import com.sfcollection.dto.*;
import com.sfcollection.model.Author;
import com.sfcollection.model.Book;
import com.sfcollection.repository.AuthorRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", 
        uses = {AuthorMapper.class, CollectionMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class BookMapper {
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @Mapping(target = "authors", source = "authors")
    @Mapping(target = "collections", source = "collections")
    public abstract BookDTO toDto(Book book);
    
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "collections", ignore = true)
    public abstract Book toEntity(BookDTO bookDTO);
    
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "collections", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateAdded", ignore = true)
    public abstract Book createDtoToEntity(BookCreateDTO createDTO);
    
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "collections", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateAdded", ignore = true)
    public abstract void updateDtoToEntity(BookUpdateDTO updateDTO, @MappingTarget Book book);
    
    @Mapping(target = "authors", ignore = true)
    @Mapping(target = "collections", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateAdded", ignore = true)
    public abstract void patchDtoToEntity(BookPatchDTO patchDTO, @MappingTarget Book book);
    
    public abstract List<BookDTO> toDtoList(List<Book> books);
    
    public Page<BookDTO> toDtoPage(Page<Book> books) {
        return books.map(this::toDto);
    }
    
    public abstract BookSummaryDTO toSummaryDto(Book book);
    
    public abstract List<BookSummaryDTO> toSummaryDtoList(List<Book> books);
    
    @AfterMapping
    protected void setAuthorsFromIds(BookCreateDTO createDTO, @MappingTarget Book book) {
        if (createDTO.getAuthorIds() != null && !createDTO.getAuthorIds().isEmpty()) {
            Set<Author> authors = createDTO.getAuthorIds().stream()
                    .map(id -> authorRepository.findById(id).orElse(null))
                    .filter(author -> author != null)
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        } else {
            book.setAuthors(new HashSet<>());
        }
    }
    
    @AfterMapping
    protected void setAuthorsFromIds(BookUpdateDTO updateDTO, @MappingTarget Book book) {
        if (updateDTO.getAuthorIds() != null) {
            Set<Author> authors = updateDTO.getAuthorIds().stream()
                    .map(id -> authorRepository.findById(id).orElse(null))
                    .filter(author -> author != null)
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }
    }
    
    @AfterMapping
    protected void setAuthorsFromIds(BookPatchDTO patchDTO, @MappingTarget Book book) {
        if (patchDTO.getAuthorIds() != null) {
            Set<Author> authors = patchDTO.getAuthorIds().stream()
                    .map(id -> authorRepository.findById(id).orElse(null))
                    .filter(author -> author != null)
                    .collect(Collectors.toSet());
            book.setAuthors(authors);
        }
    }
}