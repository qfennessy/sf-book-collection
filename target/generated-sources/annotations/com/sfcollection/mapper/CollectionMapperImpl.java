package com.sfcollection.mapper;

import com.sfcollection.dto.BookSummaryDTO;
import com.sfcollection.dto.CollectionDTO;
import com.sfcollection.dto.CollectionSummaryDTO;
import com.sfcollection.model.Book;
import com.sfcollection.model.Collection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T21:08:41-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Homebrew)"
)
@Component
public class CollectionMapperImpl implements CollectionMapper {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public CollectionDTO toDto(Collection collection) {
        if ( collection == null ) {
            return null;
        }

        CollectionDTO.CollectionDTOBuilder collectionDTO = CollectionDTO.builder();

        collectionDTO.books( bookSetToBookSummaryDTOSet( collection.getBooks() ) );
        collectionDTO.id( collection.getId() );
        collectionDTO.name( collection.getName() );
        collectionDTO.description( collection.getDescription() );
        collectionDTO.dateCreated( collection.getDateCreated() );
        collectionDTO.lastModified( collection.getLastModified() );

        return collectionDTO.build();
    }

    @Override
    public Collection toEntity(CollectionDTO collectionDTO) {
        if ( collectionDTO == null ) {
            return null;
        }

        Collection.CollectionBuilder collection = Collection.builder();

        collection.id( collectionDTO.getId() );
        collection.name( collectionDTO.getName() );
        collection.description( collectionDTO.getDescription() );
        collection.dateCreated( collectionDTO.getDateCreated() );
        collection.lastModified( collectionDTO.getLastModified() );

        return collection.build();
    }

    @Override
    public List<CollectionDTO> toDtoList(List<Collection> collections) {
        if ( collections == null ) {
            return null;
        }

        List<CollectionDTO> list = new ArrayList<CollectionDTO>( collections.size() );
        for ( Collection collection : collections ) {
            list.add( toDto( collection ) );
        }

        return list;
    }

    @Override
    public CollectionSummaryDTO toSummaryDto(Collection collection) {
        if ( collection == null ) {
            return null;
        }

        CollectionSummaryDTO.CollectionSummaryDTOBuilder collectionSummaryDTO = CollectionSummaryDTO.builder();

        collectionSummaryDTO.bookCount( countBooks( collection.getBooks() ) );
        collectionSummaryDTO.id( collection.getId() );
        collectionSummaryDTO.name( collection.getName() );
        collectionSummaryDTO.description( collection.getDescription() );
        collectionSummaryDTO.lastModified( collection.getLastModified() );

        return collectionSummaryDTO.build();
    }

    @Override
    public Set<CollectionSummaryDTO> toSummaryDtoSet(Set<Collection> collections) {
        if ( collections == null ) {
            return null;
        }

        Set<CollectionSummaryDTO> set = new LinkedHashSet<CollectionSummaryDTO>( Math.max( (int) ( collections.size() / .75f ) + 1, 16 ) );
        for ( Collection collection : collections ) {
            set.add( toSummaryDto( collection ) );
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
