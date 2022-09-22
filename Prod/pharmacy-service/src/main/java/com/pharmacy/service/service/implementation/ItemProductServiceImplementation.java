package com.pharmacy.service.service.implementation;

import com.pharmacy.service.dto.ItemProductDto;
import com.pharmacy.service.model.ItemProduct;
import com.pharmacy.service.repository.ItemProductRepository;
import com.pharmacy.service.service.ItemProductService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ResourceNotFoundException;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;

@Service
@Slf4j
public class ItemProductServiceImplementation implements ItemProductService {

    @Autowired
    private ModelMapper modelMapper;

    private static final String PHARMACY_INDEX = "search";

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private ItemProductRepository itemProductRepository;

    @Override
    public Page<ItemProductDto> findAll(boolean productType, String sortByPrice, int pageNumber, int pageSize) {
        Pageable sortLowToHigh = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price"));
        Pageable sortHighToLow = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "price"));
        if(sortByPrice.equalsIgnoreCase("asc")) {
            return this.itemProductRepository.findAllByProductType(productType, sortLowToHigh).map(itemProduct -> this.modelMapper.map(itemProduct, ItemProductDto.class));
        }
        return this.itemProductRepository.findAllByProductType(productType, sortHighToLow).map(itemProduct -> this.modelMapper.map(itemProduct, ItemProductDto.class));
    }

    @Override
    public Page<ItemProductDto> exactSearch(String productName, int pageNumber, int pageSize) {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder().withQuery(matchPhraseQuery("product_name", productName)).withPageable(PageRequest.of(pageNumber, pageSize)).build();

        SearchHits<ItemProduct> productSearchHits = this.elasticsearchOperations.search(nativeSearchQuery, ItemProduct.class, IndexCoordinates.of(PHARMACY_INDEX));
        List<ItemProduct> searchedItems = new ArrayList<>();
        productSearchHits.forEach(productSearchHit -> searchedItems.add(productSearchHit.getContent()));
        Page<ItemProduct> itemProducts = new PageImpl<>(searchedItems, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price")), searchedItems.size());

        return itemProducts.map(itemProduct -> this.modelMapper.map(itemProduct, ItemProductDto.class));
    }

    @Override
    public Page<ItemProductDto> exactSearchByStore(Long storeId, String productName, int pageNumber, int pageSize) {
        return this.itemProductRepository.findAllByProductNameAndStoreId(productName, storeId, PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price"))).map(itemProduct -> this.modelMapper.map(itemProduct, ItemProductDto.class));
    }
    @Override
    public Page<ItemProductDto> search(String productName, boolean productType, String sortByPrice, int pageNumber, int pageSize) {
        Pageable sortLowToHigh = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "price"));
        Pageable sortHighToLow = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "price"));

        if(productName == null) {
            return this.itemProductRepository.findAllByProductNameAndProductType("", productType, sortLowToHigh).map(itemProduct -> this.modelMapper.map(itemProduct, ItemProductDto.class));
        }
        productName = productName.trim();
        productName = productName.toLowerCase();

//        int idx = productName.indexOf(' ');
//        if(idx != -1) {
//            productName = productName.substring(0, idx);
//        }

        if(sortByPrice.equalsIgnoreCase("desc")) {
            return this.itemProductRepository.findAllByProductNameAndProductType(productName, productType, sortHighToLow)
                    .map(itemProduct -> this.modelMapper.map(itemProduct, ItemProductDto.class));
        }
        return this.itemProductRepository.findAllByProductNameAndProductType(productName, productType, sortLowToHigh)
                .map(itemProduct -> this.modelMapper.map(itemProduct, ItemProductDto.class));
    }

    @Override
    public Page<ItemProductDto> getItemsByStoreId(Long storeId, int pageNumber, int pageSize) {
        return this.itemProductRepository.findAllByStoreId(storeId, PageRequest.of(pageNumber, pageSize)).map(itemProduct -> this.modelMapper.map(itemProduct, ItemProductDto.class));
    }

    @Override
    public ItemProductDto findById(String itemProductId) {
        Optional<ItemProduct> itemProduct = this.itemProductRepository.findById(itemProductId);
        if(itemProduct.isEmpty()) {
            throw new ResourceNotFoundException("ItemProduct with id: "+ itemProductId + " not found!");
        }
        return this.modelMapper.map(itemProduct, ItemProductDto.class);
    }

    @Override
    public List<String> getSuggestions(String keyword) {
        WildcardQueryBuilder queryBuilder = QueryBuilders
                .wildcardQuery("product_name", "*"+keyword+"*");


        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withFilter(queryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .build();

        SearchHits<ItemProduct> searchSuggestions =
                elasticsearchOperations.search(searchQuery,
                        ItemProduct.class,
                        IndexCoordinates.of(PHARMACY_INDEX));

        Set<String> suggestions = new HashSet<>();

        searchSuggestions.getSearchHits().forEach(searchHit->{
            suggestions.add(searchHit.getContent().getProductName());
        });
        return new ArrayList<>(suggestions);
    }
}
