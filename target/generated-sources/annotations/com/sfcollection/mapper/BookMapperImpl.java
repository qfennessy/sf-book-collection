package com.sfcollection.mapper;

import com.sfcollection.dto.BookCreateDTO;
import com.sfcollection.dto.BookDTO;
import com.sfcollection.dto.BookPatchDTO;
import com.sfcollection.dto.BookSummaryDTO;
import com.sfcollection.dto.BookUpdateDTO;
import com.sfcollection.model.Book;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-21T18:13:12-0400",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.0.z20250331-1358, environment: Java 21.0.6 (Eclipse Adoptium)"
)
@Component
public class BookMapperImpl extends BookMapper {

    @Autowired
    private AuthorMapper authorMapper;
    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public BookDTO toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDTO.BookDTOBuilder bookDTO = BookDTO.builder();

        bookDTO.authors( authorMapper.toSummaryDtoSet( book.getAuthors() ) );
        bookDTO.collections( collectionMapper.toSummaryDtoSet( book.getCollections() ) );
        bookDTO.coverImage( book.getCoverImage() );
        bookDTO.dateAdded( book.getDateAdded() );
        bookDTO.description( book.getDescription() );
        bookDTO.id( book.getId() );
        bookDTO.isbn( book.getIsbn() );
        bookDTO.language( book.getLanguage() );
        bookDTO.pageCount( book.getPageCount() );
        bookDTO.publishedDate( book.getPublishedDate() );
        bookDTO.publisher( book.getPublisher() );
        bookDTO.rating( book.getRating() );
        bookDTO.readStatus( book.getReadStatus() );
        bookDTO.subGenre( book.getSubGenre() );
        bookDTO.title( book.getTitle() );

        return bookDTO.build();
    }

    @Override
    public Book toEntity(BookDTO bookDTO) {
        if ( bookDTO == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.coverImage( bookDTO.getCoverImage() );
        book.dateAdded( bookDTO.getDateAdded() );
        book.description( bookDTO.getDescription() );
        book.id( bookDTO.getId() );
        book.isbn( bookDTO.getIsbn() );
        book.language( bookDTO.getLanguage() );
        book.pageCount( bookDTO.getPageCount() );
        book.publishedDate( bookDTO.getPublishedDate() );
        book.publisher( bookDTO.getPublisher() );
        book.rating( bookDTO.getRating() );
        book.readStatus( bookDTO.getReadStatus() );
        book.subGenre( bookDTO.getSubGenre() );
        book.title( bookDTO.getTitle() );

        return book.build();
    }

    @Override
    public Book createDtoToEntity(BookCreateDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.coverImage( createDTO.getCoverImage() );
        book.description( createDTO.getDescription() );
        book.isbn( createDTO.getIsbn() );
        book.language( createDTO.getLanguage() );
        book.pageCount( createDTO.getPageCount() );
        book.publishedDate( createDTO.getPublishedDate() );
        book.publisher( createDTO.getPublisher() );
        book.rating( createDTO.getRating() );
        book.readStatus( createDTO.getReadStatus() );
        book.subGenre( createDTO.getSubGenre() );
        book.title( createDTO.getTitle() );

        return book.build();
    }

    @Override
    public void updateDtoToEntity(BookUpdateDTO updateDTO, Book book) {
        if ( updateDTO == null ) {
            return;
        }

        if ( updateDTO.getCoverImage() != null ) {
            book.setCoverImage( updateDTO.getCoverImage() );
        }
        if ( updateDTO.getDescription() != null ) {
            book.setDescription( updateDTO.getDescription() );
        }
        if ( updateDTO.getIsbn() != null ) {
            book.setIsbn( updateDTO.getIsbn() );
        }
        if ( updateDTO.getLanguage() != null ) {
            book.setLanguage( updateDTO.getLanguage() );
        }
        if ( updateDTO.getPageCount() != null ) {
            book.setPageCount( updateDTO.getPageCount() );
        }
        if ( updateDTO.getPublishedDate() != null ) {
            book.setPublishedDate( updateDTO.getPublishedDate() );
        }
        if ( updateDTO.getPublisher() != null ) {
            book.setPublisher( updateDTO.getPublisher() );
        }
        if ( updateDTO.getRating() != null ) {
            book.setRating( updateDTO.getRating() );
        }
        if ( updateDTO.getReadStatus() != null ) {
            book.setReadStatus( updateDTO.getReadStatus() );
        }
        if ( updateDTO.getSubGenre() != null ) {
            book.setSubGenre( updateDTO.getSubGenre() );
        }
        if ( updateDTO.getTitle() != null ) {
            book.setTitle( updateDTO.getTitle() );
        }

        setAuthorsFromIds( updateDTO, book );
    }

    @Override
    public void patchDtoToEntity(BookPatchDTO patchDTO, Book book) {
        if ( patchDTO == null ) {
            return;
        }

        if ( patchDTO.getCoverImage() != null ) {
            book.setCoverImage( patchDTO.getCoverImage() );
        }
        if ( patchDTO.getDescription() != null ) {
            book.setDescription( patchDTO.getDescription() );
        }
        if ( patchDTO.getIsbn() != null ) {
            book.setIsbn( patchDTO.getIsbn() );
        }
        if ( patchDTO.getLanguage() != null ) {
            book.setLanguage( patchDTO.getLanguage() );
        }
        if ( patchDTO.getPageCount() != null ) {
            book.setPageCount( patchDTO.getPageCount() );
        }
        if ( patchDTO.getPublishedDate() != null ) {
            book.setPublishedDate( patchDTO.getPublishedDate() );
        }
        if ( patchDTO.getPublisher() != null ) {
            book.setPublisher( patchDTO.getPublisher() );
        }
        if ( patchDTO.getRating() != null ) {
            book.setRating( patchDTO.getRating() );
        }
        if ( patchDTO.getReadStatus() != null ) {
            book.setReadStatus( patchDTO.getReadStatus() );
        }
        if ( patchDTO.getSubGenre() != null ) {
            book.setSubGenre( patchDTO.getSubGenre() );
        }
        if ( patchDTO.getTitle() != null ) {
            book.setTitle( patchDTO.getTitle() );
        }

        setAuthorsFromIds( patchDTO, book );
    }

    @Override
    public List<BookDTO> toDtoList(List<Book> books) {
        if ( books == null ) {
            return null;
        }

        List<BookDTO> list = new ArrayList<BookDTO>( books.size() );
        for ( Book book : books ) {
            list.add( toDto( book ) );
        }

        return list;
    }

    @Override
    public BookSummaryDTO toSummaryDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookSummaryDTO.BookSummaryDTOBuilder bookSummaryDTO = BookSummaryDTO.builder();

        bookSummaryDTO.coverImage( book.getCoverImage() );
        bookSummaryDTO.id( book.getId() );
        bookSummaryDTO.isbn( book.getIsbn() );
        bookSummaryDTO.subGenre( book.getSubGenre() );
        bookSummaryDTO.title( book.getTitle() );

        return bookSummaryDTO.build();
    }

    @Override
    public List<BookSummaryDTO> toSummaryDtoList(List<Book> books) {
        if ( books == null ) {
            return null;
        }

        List<BookSummaryDTO> list = new ArrayList<BookSummaryDTO>( books.size() );
        for ( Book book : books ) {
            list.add( toSummaryDto( book ) );
        }

        return list;
    }
}
