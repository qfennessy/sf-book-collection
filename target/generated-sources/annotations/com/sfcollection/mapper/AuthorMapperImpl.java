package com.sfcollection.mapper;

import com.sfcollection.dto.AuthorDTO;
import com.sfcollection.dto.AuthorSummaryDTO;
import com.sfcollection.dto.BookSummaryDTO;
import com.sfcollection.model.Author;
import com.sfcollection.model.Book;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T18:13:12-0400",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class AuthorMapperImpl implements AuthorMapper {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public AuthorDTO toDto(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorDTO.AuthorDTOBuilder authorDTO = AuthorDTO.builder();

        authorDTO.books( bookSetToBookSummaryDTOSet( author.getBooks() ) );
        authorDTO.biography( author.getBiography() );
        authorDTO.birthDate( author.getBirthDate() );
        authorDTO.id( author.getId() );
        authorDTO.name( author.getName() );
        authorDTO.photoUrl( author.getPhotoUrl() );

        return authorDTO.build();
    }

    @Override
    public Author toEntity(AuthorDTO authorDTO) {
        if ( authorDTO == null ) {
            return null;
        }

        Author.AuthorBuilder author = Author.builder();

        author.biography( authorDTO.getBiography() );
        author.birthDate( authorDTO.getBirthDate() );
        author.id( authorDTO.getId() );
        author.name( authorDTO.getName() );
        author.photoUrl( authorDTO.getPhotoUrl() );

        return author.build();
    }

    @Override
    public List<AuthorDTO> toDtoList(List<Author> authors) {
        if ( authors == null ) {
            return null;
        }

        List<AuthorDTO> list = new ArrayList<AuthorDTO>( authors.size() );
        for ( Author author : authors ) {
            list.add( toDto( author ) );
        }

        return list;
    }

    @Override
    public AuthorSummaryDTO toSummaryDto(Author author) {
        if ( author == null ) {
            return null;
        }

        AuthorSummaryDTO.AuthorSummaryDTOBuilder authorSummaryDTO = AuthorSummaryDTO.builder();

        authorSummaryDTO.id( author.getId() );
        authorSummaryDTO.name( author.getName() );
        authorSummaryDTO.photoUrl( author.getPhotoUrl() );

        return authorSummaryDTO.build();
    }

    @Override
    public Set<AuthorSummaryDTO> toSummaryDtoSet(Set<Author> authors) {
        if ( authors == null ) {
            return null;
        }

        Set<AuthorSummaryDTO> set = new LinkedHashSet<AuthorSummaryDTO>( Math.max( (int) ( authors.size() / .75f ) + 1, 16 ) );
        for ( Author author : authors ) {
            set.add( toSummaryDto( author ) );
        }

        return set;
    }

    protected Set<BookSummaryDTO> bookSetToBookSummaryDTOSet(Set<Book> set) {
        if ( set == null ) {
            return null;
        }

        Set<BookSummaryDTO> set1 = new LinkedHashSet<BookSummaryDTO>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Book book : set ) {
            set1.add( bookMapper.toSummaryDto( book ) );
        }

        return set1;
    }
}
