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
    date = "2025-04-21T21:08:41-0400",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.2 (Homebrew)"
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
        bookDTO.id( book.getId() );
        bookDTO.title( book.getTitle() );
        bookDTO.isbn( book.getIsbn() );
        bookDTO.publishedDate( book.getPublishedDate() );
        bookDTO.description( book.getDescription() );
        bookDTO.coverImage( book.getCoverImage() );
        bookDTO.subGenre( book.getSubGenre() );
        bookDTO.pageCount( book.getPageCount() );
        bookDTO.publisher( book.getPublisher() );
        bookDTO.language( book.getLanguage() );
        bookDTO.rating( book.getRating() );
        bookDTO.readStatus( book.getReadStatus() );
        bookDTO.dateAdded( book.getDateAdded() );

        return bookDTO.build();
    }

    @Override
    public Book toEntity(BookDTO bookDTO) {
        if ( bookDTO == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.id( bookDTO.getId() );
        book.title( bookDTO.getTitle() );
        book.isbn( bookDTO.getIsbn() );
        book.publishedDate( bookDTO.getPublishedDate() );
        book.description( bookDTO.getDescription() );
        book.coverImage( bookDTO.getCoverImage() );
        book.subGenre( bookDTO.getSubGenre() );
        book.pageCount( bookDTO.getPageCount() );
        book.publisher( bookDTO.getPublisher() );
        book.language( bookDTO.getLanguage() );
        book.rating( bookDTO.getRating() );
        book.readStatus( bookDTO.getReadStatus() );
        book.dateAdded( bookDTO.getDateAdded() );

        return book.build();
    }

    @Override
    public Book createDtoToEntity(BookCreateDTO createDTO) {
        if ( createDTO == null ) {
            return null;
        }

        Book.BookBuilder book = Book.builder();

        book.title( createDTO.getTitle() );
        book.isbn( createDTO.getIsbn() );
        book.publishedDate( createDTO.getPublishedDate() );
        book.description( createDTO.getDescription() );
        book.coverImage( createDTO.getCoverImage() );
        book.subGenre( createDTO.getSubGenre() );
        book.pageCount( createDTO.getPageCount() );
        book.publisher( createDTO.getPublisher() );
        book.language( createDTO.getLanguage() );
        book.rating( createDTO.getRating() );
        book.readStatus( createDTO.getReadStatus() );

        return book.build();
    }

    @Override
    public void updateDtoToEntity(BookUpdateDTO updateDTO, Book book) {
        if ( updateDTO == null ) {
            return;
        }

        if ( updateDTO.getTitle() != null ) {
            book.setTitle( updateDTO.getTitle() );
        }
        if ( updateDTO.getIsbn() != null ) {
            book.setIsbn( updateDTO.getIsbn() );
        }
        if ( updateDTO.getPublishedDate() != null ) {
            book.setPublishedDate( updateDTO.getPublishedDate() );
        }
        if ( updateDTO.getDescription() != null ) {
            book.setDescription( updateDTO.getDescription() );
        }
        if ( updateDTO.getCoverImage() != null ) {
            book.setCoverImage( updateDTO.getCoverImage() );
        }
        if ( updateDTO.getSubGenre() != null ) {
            book.setSubGenre( updateDTO.getSubGenre() );
        }
        if ( updateDTO.getPageCount() != null ) {
            book.setPageCount( updateDTO.getPageCount() );
        }
        if ( updateDTO.getPublisher() != null ) {
            book.setPublisher( updateDTO.getPublisher() );
        }
        if ( updateDTO.getLanguage() != null ) {
            book.setLanguage( updateDTO.getLanguage() );
        }
        if ( updateDTO.getRating() != null ) {
            book.setRating( updateDTO.getRating() );
        }
        if ( updateDTO.getReadStatus() != null ) {
            book.setReadStatus( updateDTO.getReadStatus() );
        }

        setAuthorsFromIds( updateDTO, book );
    }

    @Override
    public void patchDtoToEntity(BookPatchDTO patchDTO, Book book) {
        if ( patchDTO == null ) {
            return;
        }

        if ( patchDTO.getTitle() != null ) {
            book.setTitle( patchDTO.getTitle() );
        }
        if ( patchDTO.getIsbn() != null ) {
            book.setIsbn( patchDTO.getIsbn() );
        }
        if ( patchDTO.getPublishedDate() != null ) {
            book.setPublishedDate( patchDTO.getPublishedDate() );
        }
        if ( patchDTO.getDescription() != null ) {
            book.setDescription( patchDTO.getDescription() );
        }
        if ( patchDTO.getCoverImage() != null ) {
            book.setCoverImage( patchDTO.getCoverImage() );
        }
        if ( patchDTO.getSubGenre() != null ) {
            book.setSubGenre( patchDTO.getSubGenre() );
        }
        if ( patchDTO.getPageCount() != null ) {
            book.setPageCount( patchDTO.getPageCount() );
        }
        if ( patchDTO.getPublisher() != null ) {
            book.setPublisher( patchDTO.getPublisher() );
        }
        if ( patchDTO.getLanguage() != null ) {
            book.setLanguage( patchDTO.getLanguage() );
        }
        if ( patchDTO.getRating() != null ) {
            book.setRating( patchDTO.getRating() );
        }
        if ( patchDTO.getReadStatus() != null ) {
            book.setReadStatus( patchDTO.getReadStatus() );
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

        bookSummaryDTO.id( book.getId() );
        bookSummaryDTO.title( book.getTitle() );
        bookSummaryDTO.isbn( book.getIsbn() );
        bookSummaryDTO.subGenre( book.getSubGenre() );
        bookSummaryDTO.coverImage( book.getCoverImage() );

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
