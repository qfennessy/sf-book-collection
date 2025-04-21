package com.sfcollection.service.impl;

import com.sfcollection.dto.CollectionDTO;
import com.sfcollection.exception.ResourceNotFoundException;
import com.sfcollection.mapper.CollectionMapper;
import com.sfcollection.model.Book;
import com.sfcollection.model.Collection;
import com.sfcollection.repository.BookRepository;
import com.sfcollection.repository.CollectionRepository;
import com.sfcollection.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionServiceImpl implements CollectionService {
    
    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;
    private final CollectionMapper collectionMapper;
    
    @Override
    @Transactional
    public CollectionDTO createCollection(CollectionDTO collectionDTO) {
        Collection collection = collectionMapper.toEntity(collectionDTO);
        Collection savedCollection = collectionRepository.save(collection);
        return collectionMapper.toDto(savedCollection);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CollectionDTO getCollectionById(Long id) {
        Collection collection = findCollectionById(id);
        return collectionMapper.toDto(collection);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CollectionDTO> getAllCollections() {
        List<Collection> collections = collectionRepository.findAll();
        return collectionMapper.toDtoList(collections);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CollectionDTO> searchCollectionsByName(String name) {
        List<Collection> collections = collectionRepository.findByNameContainingIgnoreCase(name);
        return collectionMapper.toDtoList(collections);
    }
    
    @Override
    @Transactional
    public CollectionDTO updateCollection(Long id, CollectionDTO collectionDTO) {
        Collection existingCollection = findCollectionById(id);
        
        existingCollection.setName(collectionDTO.getName());
        existingCollection.setDescription(collectionDTO.getDescription());
        
        Collection updatedCollection = collectionRepository.save(existingCollection);
        return collectionMapper.toDto(updatedCollection);
    }
    
    @Override
    @Transactional
    public void deleteCollection(Long id) {
        if (!collectionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Collection not found with id: " + id);
        }
        collectionRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public CollectionDTO addBookToCollection(Long collectionId, Long bookId) {
        Collection collection = findCollectionById(collectionId);
        Book book = findBookById(bookId);
        
        collection.getBooks().add(book);
        book.getCollections().add(collection);
        
        Collection updatedCollection = collectionRepository.save(collection);
        return collectionMapper.toDto(updatedCollection);
    }
    
    @Override
    @Transactional
    public CollectionDTO addBooksToCollection(Long collectionId, List<Long> bookIds) {
        Collection collection = findCollectionById(collectionId);
        
        List<Book> booksToAdd = new ArrayList<>();
        for (Long bookId : bookIds) {
            Book book = findBookById(bookId);
            booksToAdd.add(book);
        }
        
        for (Book book : booksToAdd) {
            collection.getBooks().add(book);
            book.getCollections().add(collection);
        }
        
        Collection updatedCollection = collectionRepository.save(collection);
        return collectionMapper.toDto(updatedCollection);
    }
    
    @Override
    @Transactional
    public CollectionDTO removeBookFromCollection(Long collectionId, Long bookId) {
        Collection collection = findCollectionById(collectionId);
        Book book = findBookById(bookId);
        
        collection.getBooks().remove(book);
        book.getCollections().remove(collection);
        
        Collection updatedCollection = collectionRepository.save(collection);
        return collectionMapper.toDto(updatedCollection);
    }
    
    private Collection findCollectionById(Long id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + id));
    }
    
    private Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }
}