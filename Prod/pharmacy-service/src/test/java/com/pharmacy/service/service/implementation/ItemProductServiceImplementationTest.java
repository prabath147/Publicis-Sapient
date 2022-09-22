package com.pharmacy.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pharmacy.service.dto.ItemProductDto;
import com.pharmacy.service.model.ItemProduct;
import com.pharmacy.service.repository.ItemProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.elasticsearch.ResourceNotFoundException;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.TotalHitsRelation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.suggest.response.Suggest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {ItemProductServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class ItemProductServiceImplementationTest {
    @MockBean
    private ElasticsearchOperations elasticsearchOperations;

    @MockBean
    private ItemProductRepository itemProductRepository;

    @Autowired
    private ItemProductServiceImplementation itemProductServiceImplementation;

    @MockBean
    private ModelMapper modelMapper;

    /**
     * Method under test: {@link ItemProductServiceImplementation#findAll(boolean, String, int, int)}
     */
    @Test
    void testFindAll() {
        when(itemProductRepository.findAllByProductType(anyBoolean(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(itemProductServiceImplementation.findAll(true, "Sort By Price", 10, 3).toList().isEmpty());
        verify(itemProductRepository).findAllByProductType(anyBoolean(), (Pageable) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#findAll(boolean, String, int, int)}
     */
    @Test
    void testFindAll2() {
        ArrayList<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct());
        PageImpl<ItemProduct> pageImpl = new PageImpl<>(itemProductList);
        when(itemProductRepository.findAllByProductType(anyBoolean(), (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());
        assertEquals(1, itemProductServiceImplementation.findAll(true, "Sort By Price", 10, 3).toList().size());
        verify(itemProductRepository).findAllByProductType(anyBoolean(), (Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<ItemProductDto>) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#findAll(boolean, String, int, int)}
     */
    @Test
    void testFindAll3() {
        ArrayList<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct());
        itemProductList.add(new ItemProduct());
        PageImpl<ItemProduct> pageImpl = new PageImpl<>(itemProductList);
        when(itemProductRepository.findAllByProductType(anyBoolean(), (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());
        assertEquals(2, itemProductServiceImplementation.findAll(true, "Sort By Price", 10, 3).toList().size());
        verify(itemProductRepository).findAllByProductType(anyBoolean(), (Pageable) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<ItemProductDto>) any());
    }


    /**
     * Method under test: {@link ItemProductServiceImplementation#exactSearch(String, int, int)}
     */
    @Test
    void testExactSearch() {
        ArrayList<SearchHit<ItemProduct>> searchHits = new ArrayList<>();
        when(elasticsearchOperations.search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any()))
                .thenReturn(new SearchHitsImpl<>(1L, TotalHitsRelation.EQUAL_TO, 10.0f, "42", searchHits, null,
                        new Suggest(new ArrayList<>(), true)));
        assertTrue(itemProductServiceImplementation.exactSearch("Product Name", 10, 3).toList().isEmpty());
        verify(elasticsearchOperations).search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#exactSearch(String, int, int)}
     */
    @Test
    void testExactSearch2() {
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());

        ArrayList<SearchHit<ItemProduct>> searchHitList = new ArrayList<>();
        HashMap<String, List<String>> highlightFields = new HashMap<>();
        searchHitList.add(new SearchHit<>("product_name", "42", "product_name", 10.0f, new Object[]{"Sort Values"},
                highlightFields, new ItemProduct()));
        SearchHitsImpl<ItemProduct> searchHitsImpl = new SearchHitsImpl<>(1L, TotalHitsRelation.EQUAL_TO, 10.0f, "42",
                searchHitList, null, new Suggest(new ArrayList<>(), true));

        when(elasticsearchOperations.search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any()))
                .thenReturn(searchHitsImpl);
        assertEquals(1, itemProductServiceImplementation.exactSearch("Product Name", 10, 3).toList().size());
        verify(modelMapper).map((Object) any(), (Class<ItemProductDto>) any());
        verify(elasticsearchOperations).search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#exactSearch(String, int, int)}
     */
    @Test
    void testExactSearch3() {
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());

        ArrayList<SearchHit<ItemProduct>> searchHitList = new ArrayList<>();
        HashMap<String, List<String>> highlightFields = new HashMap<>();
        searchHitList.add(new SearchHit<>("product_name", "42", "product_name", 10.0f, new Object[]{"Sort Values"},
                highlightFields, new ItemProduct()));
        HashMap<String, List<String>> highlightFields1 = new HashMap<>();
        searchHitList.add(new SearchHit<>("product_name", "42", "product_name", 10.0f, new Object[]{"Sort Values"},
                highlightFields1, new ItemProduct()));
        SearchHitsImpl<ItemProduct> searchHitsImpl = new SearchHitsImpl<>(1L, TotalHitsRelation.EQUAL_TO, 10.0f, "42",
                searchHitList, null, new Suggest(new ArrayList<>(), true));

        when(elasticsearchOperations.search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any()))
                .thenReturn(searchHitsImpl);
        assertEquals(2, itemProductServiceImplementation.exactSearch("Product Name", 10, 3).toList().size());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<ItemProductDto>) any());
        verify(elasticsearchOperations).search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#exactSearchByStore(Long, String, int, int)}
     */
    @Test
    void testExactSearchByStore() {
        when(itemProductRepository.findAllByProductNameAndStoreId((String) any(), (Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(itemProductServiceImplementation.exactSearchByStore(123L, "Product Name", 10, 3).toList().isEmpty());
        verify(itemProductRepository).findAllByProductNameAndStoreId((String) any(), (Long) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#exactSearchByStore(Long, String, int, int)}
     */
    @Test
    void testExactSearchByStore2() {
        ArrayList<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct());
        PageImpl<ItemProduct> pageImpl = new PageImpl<>(itemProductList);
        when(itemProductRepository.findAllByProductNameAndStoreId((String) any(), (Long) any(), (Pageable) any()))
                .thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());
        assertEquals(1, itemProductServiceImplementation.exactSearchByStore(123L, "Product Name", 10, 3).toList().size());
        verify(itemProductRepository).findAllByProductNameAndStoreId((String) any(), (Long) any(), (Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<ItemProductDto>) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#exactSearchByStore(Long, String, int, int)}
     */
    @Test
    void testExactSearchByStore3() {
        ArrayList<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct());
        itemProductList.add(new ItemProduct());
        PageImpl<ItemProduct> pageImpl = new PageImpl<>(itemProductList);
        when(itemProductRepository.findAllByProductNameAndStoreId((String) any(), (Long) any(), (Pageable) any()))
                .thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());
        assertEquals(2, itemProductServiceImplementation.exactSearchByStore(123L, "Product Name", 10, 3).toList().size());
        verify(itemProductRepository).findAllByProductNameAndStoreId((String) any(), (Long) any(), (Pageable) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<ItemProductDto>) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#search(String, boolean, String, int, int)}
     */
    @Test
    void testSearch() {
        when(itemProductRepository.findAllByProductNameAndProductType((String) any(), anyBoolean(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(
                itemProductServiceImplementation.search("Product Name", true, "Sort By Price", 10, 3).toList().isEmpty());
        verify(itemProductRepository).findAllByProductNameAndProductType((String) any(), anyBoolean(), (Pageable) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#search(String, boolean, String, int, int)}
     */
    @Test
    void testSearch2() {
        ArrayList<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct());
        PageImpl<ItemProduct> pageImpl = new PageImpl<>(itemProductList);
        when(itemProductRepository.findAllByProductNameAndProductType((String) any(), anyBoolean(), (Pageable) any()))
                .thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());
        assertEquals(1,
                itemProductServiceImplementation.search("Product Name", true, "Sort By Price", 10, 3).toList().size());
        verify(itemProductRepository).findAllByProductNameAndProductType((String) any(), anyBoolean(), (Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<ItemProductDto>) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#search(String, boolean, String, int, int)}
     */
    @Test
    void testSearch3() {
        ArrayList<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct());
        itemProductList.add(new ItemProduct());
        PageImpl<ItemProduct> pageImpl = new PageImpl<>(itemProductList);
        when(itemProductRepository.findAllByProductNameAndProductType((String) any(), anyBoolean(), (Pageable) any()))
                .thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());
        assertEquals(2,
                itemProductServiceImplementation.search("Product Name", true, "Sort By Price", 10, 3).toList().size());
        verify(itemProductRepository).findAllByProductNameAndProductType((String) any(), anyBoolean(), (Pageable) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<ItemProductDto>) any());
    }



    @Test
    void testGetItemsByStoreId() {
        when(itemProductRepository.findAllByStoreId((Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        assertTrue(itemProductServiceImplementation.getItemsByStoreId(123L, 10, 3).toList().isEmpty());
        verify(itemProductRepository).findAllByStoreId((Long) any(), (Pageable) any());
    }


    @Test
    void testGetItemsByStoreId2() {
        ArrayList<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct());
        PageImpl<ItemProduct> pageImpl = new PageImpl<>(itemProductList);
        when(itemProductRepository.findAllByStoreId((Long) any(), (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());
        assertEquals(1, itemProductServiceImplementation.getItemsByStoreId(123L, 10, 3).toList().size());
        verify(itemProductRepository).findAllByStoreId((Long) any(), (Pageable) any());
        verify(modelMapper).map((Object) any(), (Class<ItemProductDto>) any());
    }


    @Test
    void testGetItemsByStoreId3() {
        ArrayList<ItemProduct> itemProductList = new ArrayList<>();
        itemProductList.add(new ItemProduct());
        itemProductList.add(new ItemProduct());
        PageImpl<ItemProduct> pageImpl = new PageImpl<>(itemProductList);
        when(itemProductRepository.findAllByStoreId((Long) any(), (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ItemProductDto>) any())).thenReturn(new ItemProductDto());
        assertEquals(2, itemProductServiceImplementation.getItemsByStoreId(123L, 10, 3).toList().size());
        verify(itemProductRepository).findAllByStoreId((Long) any(), (Pageable) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<ItemProductDto>) any());
    }


    @Test
    void testFindById() {
        when(itemProductRepository.findById((String) any())).thenReturn(Optional.empty());
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
        assertThrows(ResourceNotFoundException.class, () -> itemProductServiceImplementation.findById("42"));
        verify(itemProductRepository).findById((String) any());
    }


    @Test
    void testFindById2() {
        when(itemProductRepository.findById((String) any())).thenReturn(Optional.of(new ItemProduct()));
        ItemProductDto itemProductDto = new ItemProductDto();
        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn(itemProductDto);
        assertSame(itemProductDto, itemProductServiceImplementation.findById("42"));
        verify(itemProductRepository).findById((String) any());
        verify(modelMapper).map((Object) any(), (Class<Object>) any());
    }


    @Test
    void testGetSuggestions() {
        ArrayList<SearchHit<ItemProduct>> searchHits = new ArrayList<>();
        when(elasticsearchOperations.search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any()))
                .thenReturn(new SearchHitsImpl<>(1L, TotalHitsRelation.EQUAL_TO, 10.0f, "42", searchHits, null,
                        new Suggest(new ArrayList<>(), true)));
        assertTrue(itemProductServiceImplementation.getSuggestions("Keyword").isEmpty());
        verify(elasticsearchOperations).search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any());
    }


    @Test
    void testGetSuggestions2() {
        ArrayList<SearchHit<ItemProduct>> searchHitList = new ArrayList<>();
        HashMap<String, List<String>> highlightFields = new HashMap<>();
        searchHitList.add(new SearchHit<>("product_name", "42", "product_name", 10.0f, new Object[]{"Sort Values"},
                highlightFields, new ItemProduct()));
        SearchHitsImpl<ItemProduct> searchHitsImpl = new SearchHitsImpl<>(1L, TotalHitsRelation.EQUAL_TO, 10.0f, "42",
                searchHitList, null, new Suggest(new ArrayList<>(), true));

        when(elasticsearchOperations.search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any()))
                .thenReturn(searchHitsImpl);
        List<String> actualSuggestions = itemProductServiceImplementation.getSuggestions("Keyword");
        assertEquals(1, actualSuggestions.size());
        assertNull(actualSuggestions.get(0));
        verify(elasticsearchOperations).search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any());
    }

    /**
     * Method under test: {@link ItemProductServiceImplementation#getSuggestions(String)}
     */
    @Test
    void testGetSuggestions3() {
        ArrayList<SearchHit<ItemProduct>> searchHitList = new ArrayList<>();
        HashMap<String, List<String>> highlightFields = new HashMap<>();
        searchHitList.add(new SearchHit<>("product_name", "42", "product_name", 10.0f, new Object[]{"Sort Values"},
                highlightFields, new ItemProduct()));
        HashMap<String, List<String>> highlightFields1 = new HashMap<>();
        searchHitList.add(new SearchHit<>("product_name", "42", "product_name", 10.0f, new Object[]{"Sort Values"},
                highlightFields1, new ItemProduct()));
        SearchHitsImpl<ItemProduct> searchHitsImpl = new SearchHitsImpl<>(1L, TotalHitsRelation.EQUAL_TO, 10.0f, "42",
                searchHitList, null, new Suggest(new ArrayList<>(), true));

        when(elasticsearchOperations.search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any()))
                .thenReturn(searchHitsImpl);
        List<String> actualSuggestions = itemProductServiceImplementation.getSuggestions("Keyword");
        assertEquals(1, actualSuggestions.size());
        assertNull(actualSuggestions.get(0));
        verify(elasticsearchOperations).search((Query) any(), (Class<ItemProduct>) any(), (IndexCoordinates) any());
    }
}

