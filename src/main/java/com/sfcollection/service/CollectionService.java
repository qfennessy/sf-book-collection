package com.sfcollection.service;

import com.sfcollection.dto.CollectionDTO;

import java.util.List;

public interface CollectionService {
    CollectionDTO createCollection(CollectionDTO collectionDTO);
    CollectionDTO getCollectionById(Long id);
    List<CollectionDTO> getAllCollections();
    List<CollectionDTO> searchCollectionsByName(String name);
    CollectionDTO updateCollection(Long id, CollectionDTO collectionDTO);
    void deleteCollection(Long id);
    CollectionDTO addBookToCollection(Long collectionId, Long bookId);
    CollectionDTO addBooksToCollection(Long collectionId, List<Long> bookIds);
    CollectionDTO removeBookFromCollection(Long collectionId, Long bookId);
}