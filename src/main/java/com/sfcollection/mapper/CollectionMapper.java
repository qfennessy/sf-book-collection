package com.sfcollection.mapper;

import com.sfcollection.dto.CollectionDTO;
import com.sfcollection.dto.CollectionSummaryDTO;
import com.sfcollection.model.Collection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {BookMapper.class})
public interface CollectionMapper {
    
    @Mapping(target = "books", source = "books")
    CollectionDTO toDto(Collection collection);
    
    @Mapping(target = "books", ignore = true)
    Collection toEntity(CollectionDTO collectionDTO);
    
    List<CollectionDTO> toDtoList(List<Collection> collections);
    
    @Mapping(target = "bookCount", source = "books", qualifiedByName = "countBooks")
    CollectionSummaryDTO toSummaryDto(Collection collection);
    
    Set<CollectionSummaryDTO> toSummaryDtoSet(Set<Collection> collections);
    
    @Named("countBooks")
    default int countBooks(Set<?> books) {
        return books != null ? books.size() : 0;
    }
}