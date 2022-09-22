package com.pharmacy.service.service.implementation;

import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.StoreDto;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.Store;
import com.pharmacy.service.repository.StoreRepository;
import com.pharmacy.service.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class StoreServiceImplementation implements StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public StoreDto getStore(Long storeId) {
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if (optionalStore.isEmpty())
            throw new ResourceException("Store", "ID", storeId, ResourceException.ErrorType.FOUND);
        return modelMapper.map(optionalStore.get(), StoreDto.class);
    }

    @Override
    public Long noOfStores(Long managerId) {
        return storeRepository.countAllByManager_ManagerId(managerId);
    }

    @Override
    public Long noOfStores() {
        return storeRepository.count();
    }

    @Override
    public Double totalRevenue(Long managerId) {
        Double revenue = 0D;
        for (Store store: storeRepository.findAllByManager_ManagerId(managerId)) revenue += store.getRevenue();
        return revenue;
    }

    @Override
    @Async("asyncExecutor")
    public void getStoreAsync(Long storeId) {
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if (optionalStore.isEmpty())
            throw new ResourceException("Store", "ID", storeId, ResourceException.ErrorType.FOUND);
        CompletableFuture.completedFuture(modelMapper.map(optionalStore.get(), StoreDto.class));
    }

    private PageableResponse<StoreDto> getPageableResponse(Page<Store> storePage) {
        List<Store> allStores = storePage.getContent();

        List<StoreDto> storeDtoList = new ArrayList<>();
        for (Store store : allStores)
            storeDtoList.add(modelMapper.map(store, StoreDto.class));

        PageableResponse<StoreDto> pageableStoreResponse = new PageableResponse<>();
        return pageableStoreResponse.setResponseData(storeDtoList, storePage);
    }

    @Override
    public PageableResponse<StoreDto> getStores(Integer pageNumber, Integer pageSize) {
        return getPageableResponse(storeRepository.findAll(PageRequest.of(pageNumber, pageSize)));
    }

    @Override
    public boolean checkIfExists(Long storeId) {
        return storeRepository.existsById(storeId);
    }

    @Override
    public PageableResponse<StoreDto> getManagerStore(Long managerId, Integer pageNumber, Integer pageSize) {
        return getPageableResponse(storeRepository.findAllByManager_ManagerId(managerId, PageRequest.of(pageNumber, pageSize)));
    }


    @Override
    public List<Store> getStoresByManagerToDelete(Long managerId) {
        return storeRepository.findAllByManager_ManagerId(managerId);
    }


    @Override
    public StoreDto createStore(StoreDto storeDto) {
        Store store = modelMapper.map(storeDto, Store.class);
        try {
            return modelMapper.map(storeRepository.save(store), StoreDto.class);
        } catch (Exception e) {
            throw new ResourceException("Store", "store", store, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public StoreDto updateStore(StoreDto storeDto) {
        Store newStore = modelMapper.map(storeDto, Store.class);
        Optional<Store> optionalStore = storeRepository.findById(newStore.getStoreId());
        if (optionalStore.isEmpty())
            throw new ResourceException("Store", "store", storeDto, ResourceException.ErrorType.FOUND);
        try {
            return modelMapper.map(storeRepository.save(newStore), StoreDto.class);
        } catch (Exception e) {
            throw new ResourceException("Store", "store", storeDto, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public void updateStore(Set<StoreDto> storeDtoList) {
        List<Store> storeList = new ArrayList<>();
        for (StoreDto storeDto : storeDtoList) {
            storeList.add(modelMapper.map(storeDto, Store.class));
        }
        try {
            storeRepository.saveAll(storeList);
        } catch (Exception e) {
            throw new ResourceException("Store", "store list", storeDtoList, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public void deleteStore(Long storeId) {
        Optional<Store> optionalStore = storeRepository.findById(storeId);
        if (optionalStore.isEmpty())
            throw new ResourceException("Store", "ID", storeId, ResourceException.ErrorType.FOUND);
        try {
            storeRepository.deleteById(storeId);
        } catch (Exception e) {
            throw new ResourceException("Store", "ID", storeId, ResourceException.ErrorType.DELETED, e);
        }
    }
}
