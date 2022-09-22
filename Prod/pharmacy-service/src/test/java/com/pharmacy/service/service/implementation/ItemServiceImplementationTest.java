package com.pharmacy.service.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.pharmacy.service.client.auth.AuthClient;
import com.pharmacy.service.client.notify.NotificationClient;
import com.pharmacy.service.dto.*;
import com.pharmacy.service.dtoexternal.UserNotificationDto;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.Address;
import com.pharmacy.service.model.ApprovalStatus;
import com.pharmacy.service.model.Item;
import com.pharmacy.service.model.Manager;
import com.pharmacy.service.model.Product;
import com.pharmacy.service.model.Store;
import com.pharmacy.service.repository.ItemRepository;
import com.pharmacy.service.service.ProductService;
import com.pharmacy.service.service.StoreService;
import com.pharmacy.service.utils.ItemMini;

import java.text.ParseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.pharmacy.service.utils.JwtUtils;
import com.pharmacy.service.utils.SchedulerTasks;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;

@ContextConfiguration(classes = {ItemServiceImplementation.class})
@ExtendWith(SpringExtension.class)
class ItemServiceImplementationTest {
    @MockBean
    private AuthClient authClient;

    @MockBean
    private ItemRepository itemRepository;

    @Autowired
    private ItemServiceImplementation itemServiceImplementation;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private NotificationClient notificationClient;

    @MockBean
    private ProductService productService;

    @MockBean
    private StoreService storeService;

    @MockBean
    private SchedulerTasks schedulerTasks;

    Product mockProduct;
    Address mockAddress;
    Manager mockManager;
    Store mockStore;
    Item mockItem;

    ProductDto mockProductDto;
    AddressDto mockAddressDto;
    ManagerDto mockManagerDto;
    StoreDto mockStoreDto;
    ItemDto mockItemDto;


    @BeforeEach
    void setup() {
        mockProduct = new Product();
        mockProduct.setCategorySet(new HashSet<>());
        mockProduct.setDescription("The characteristics of someone or something");
        mockProduct.setDosageForm("Dosage Form");
        mockProduct.setImageUrl("https://example.org/example");
        mockProduct.setProductId(123L);
        mockProduct.setProductName("Product Name");
        mockProduct.setProductType(true);
        mockProduct.setProprietaryName("Proprietary Name");
        mockProduct.setQuantity(1L);

        mockAddress = new Address();
        mockAddress.setAddressId(123L);
        mockAddress.setCity("Oxford");
        mockAddress.setCountry("GB");
        mockAddress.setPinCode(1);
        mockAddress.setState("MD");
        mockAddress.setStreet("Street");

        mockManager = new Manager();
        mockManager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        mockManager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        mockManager.setLicenseNo("License No");
        mockManager.setManagerId(123L);
        mockManager.setName("Name");
        mockManager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mockManager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        mockStore = new Store();
        mockStore.setAddress(mockAddress);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mockStore.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        mockStore.setManager(mockManager);
        mockStore.setRevenue(10.0d);
        mockStore.setStoreId(123L);
        mockStore.setStoreName("Store Name");

        mockItem = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mockItem.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mockItem.setItemId(123L);
        mockItem.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        mockItem.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        mockItem.setPrice(10.0d);
        mockItem.setProduct(mockProduct);
        mockItem.setStore(mockStore);

        // DTO

        mockProductDto = new ProductDto();
        mockProductDto.setCategorySet(new HashSet<>());
        mockProductDto.setDescription("The characteristics of someone or something");
        mockProductDto.setDosageForm("Dosage Form");
        mockProductDto.setImageUrl("https://example.org/example");
        mockProductDto.setProductId(123L);
        mockProductDto.setProductName("Product Name");
        mockProductDto.setProductType(true);
        mockProductDto.setProprietaryName("Proprietary Name");
        mockProductDto.setQuantity(1L);

        mockAddressDto = new AddressDto();
        mockAddressDto.setAddressId(123L);
        mockAddressDto.setCity("Oxford");
        mockAddressDto.setCountry("GB");
        mockAddressDto.setPinCode(1);
        mockAddressDto.setState("MD");
        mockAddressDto.setStreet("Street");

        mockManagerDto = new ManagerDto();
        mockManagerDto.setApprovalStatus(ApprovalStatus.APPROVED);
        mockManagerDto.setLicenseNo("License No");
        mockManagerDto.setManagerId(123L);
        mockManagerDto.setName("Name");
        mockManagerDto.setPhoneNo("4105551212");
        mockManager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        mockStoreDto = new StoreDto();
        mockStoreDto.setAddress(mockAddressDto);
        mockStoreDto.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        mockStoreDto.setManager(mockManagerDto);
        mockStoreDto.setRevenue(10.0d);
        mockStoreDto.setStoreId(123L);
        mockStoreDto.setStoreName("Store Name");

        mockItemDto = new ItemDto();
        mockItemDto.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        mockItemDto.setItemId(123L);
        mockItemDto.setItemQuantity(42L);
        mockItemDto.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        mockItemDto.setPrice(10.0d);
        mockItemDto.setProduct(mockProductDto);
        mockItemDto.setStore(mockStoreDto);
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItem(Long)}
     */
    @Test
    void testGetItem() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        ItemDto itemDto = new ItemDto();
        when(modelMapper.map((Object) any(), (Class<ItemDto>) any())).thenReturn(itemDto);
        assertSame(itemDto, itemServiceImplementation.getItem(123L));
        verify(itemRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<ItemDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItem(Long)}
     */
    @Test
    void testGetItem2() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<ItemDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getItem(123L));
        verify(itemRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<ItemDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsWithoutStoreInfo(Integer, Integer)}
     */
    @Test
    void testGetItemsWithoutStoreInfo() {
        when(itemRepository.findBy((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        PageableResponse<ItemDto> actualItemsWithoutStoreInfo = itemServiceImplementation.getItemsWithoutStoreInfo(10, 3);
        assertTrue(actualItemsWithoutStoreInfo.getData().isEmpty());
        assertEquals(0L, actualItemsWithoutStoreInfo.getTotalRecords().longValue());
        assertEquals(1, actualItemsWithoutStoreInfo.getTotalPages().intValue());
        assertEquals(0, actualItemsWithoutStoreInfo.getPageSize().intValue());
        assertEquals(0, actualItemsWithoutStoreInfo.getPageNumber().intValue());
        assertTrue(actualItemsWithoutStoreInfo.getIsLastPage());
        verify(itemRepository).findBy((Pageable) any());
    }


    /**
     * Method under test: {@link ItemServiceImplementation#getItemsWithoutStoreInfo(Integer, Integer)}
     */
    @Test
    void testGetItemsWithoutStoreInfo4() {
        when(itemRepository.findBy((Pageable) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getItemsWithoutStoreInfo(10, 3));
        verify(itemRepository).findBy((Pageable) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsWithoutStoreInfo(Integer, Integer)}
     */
    @Test
    void testGetItemsWithoutStoreInfo5() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        ItemMini itemMini = mock(ItemMini.class);
        when(itemMini.getProduct()).thenReturn(product);
        when(itemMini.getPrice()).thenReturn(10.0d);
        when(itemMini.getItemId()).thenReturn(123L);
        when(itemMini.getItemQuantity()).thenReturn(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getExpiryDate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getManufacturedDate())
                .thenReturn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<ItemMini> itemMiniList = new ArrayList<>();
        itemMiniList.add(itemMini);
        PageImpl<ItemMini> pageImpl = new PageImpl<>(itemMiniList);
        when(itemRepository.findBy((Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenReturn(new ProductDto());
        PageableResponse<ItemDto> actualItemsWithoutStoreInfo = itemServiceImplementation.getItemsWithoutStoreInfo(10, 3);
        assertEquals(1, actualItemsWithoutStoreInfo.getData().size());
        assertEquals(1L, actualItemsWithoutStoreInfo.getTotalRecords().longValue());
        assertEquals(1, actualItemsWithoutStoreInfo.getTotalPages().intValue());
        assertEquals(1, actualItemsWithoutStoreInfo.getPageSize().intValue());
        assertEquals(0, actualItemsWithoutStoreInfo.getPageNumber().intValue());
        assertTrue(actualItemsWithoutStoreInfo.getIsLastPage());
        verify(itemRepository).findBy((Pageable) any());
        verify(itemMini).getProduct();
        verify(itemMini).getPrice();
        verify(itemMini).getItemId();
        verify(itemMini).getItemQuantity();
        verify(itemMini).getExpiryDate();
        verify(itemMini).getManufacturedDate();
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsWithoutStoreInfo(Integer, Integer)}
     */
    @Test
    void testGetItemsWithoutStoreInfo6() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        ItemMini itemMini = mock(ItemMini.class);
        when(itemMini.getProduct()).thenReturn(product);
        when(itemMini.getPrice()).thenReturn(10.0d);
        when(itemMini.getItemId()).thenReturn(123L);
        when(itemMini.getItemQuantity()).thenReturn(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getExpiryDate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getManufacturedDate())
                .thenReturn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<ItemMini> itemMiniList = new ArrayList<>();
        itemMiniList.add(itemMini);
        PageImpl<ItemMini> pageImpl = new PageImpl<>(itemMiniList);
        when(itemRepository.findBy((Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getItemsWithoutStoreInfo(10, 3));
        verify(itemRepository).findBy((Pageable) any());
        verify(itemMini).getProduct();
        verify(itemMini).getItemId();
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsByStore(Long, Integer, Integer)}
     */
    @Test
    void testGetItemsByStore() {
        when(itemRepository.findAllByStore_StoreId((Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        PageableResponse<ItemDto> actualItemsByStore = itemServiceImplementation.getItemsByStore(123L, 10, 3);
        assertTrue(actualItemsByStore.getData().isEmpty());
        assertEquals(0L, actualItemsByStore.getTotalRecords().longValue());
        assertEquals(1, actualItemsByStore.getTotalPages().intValue());
        assertEquals(0, actualItemsByStore.getPageSize().intValue());
        assertEquals(0, actualItemsByStore.getPageNumber().intValue());
        assertTrue(actualItemsByStore.getIsLastPage());
        verify(itemRepository).findAllByStore_StoreId((Long) any(), (Pageable) any());
    }

    @Test
    void testGetItemsByStore4() {
        when(itemRepository.findAllByStore_StoreId((Long) any(), (Pageable) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getItemsByStore(123L, 10, 3));
        verify(itemRepository).findAllByStore_StoreId((Long) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsByStore(Long, Integer, Integer)}
     */
    @Test
    void testGetItemsByStore5() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        ItemMini itemMini = mock(ItemMini.class);
        when(itemMini.getProduct()).thenReturn(product);
        when(itemMini.getPrice()).thenReturn(10.0d);
        when(itemMini.getItemId()).thenReturn(123L);
        when(itemMini.getItemQuantity()).thenReturn(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getExpiryDate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getManufacturedDate())
                .thenReturn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<ItemMini> itemMiniList = new ArrayList<>();
        itemMiniList.add(itemMini);
        PageImpl<ItemMini> pageImpl = new PageImpl<>(itemMiniList);
        when(itemRepository.findAllByStore_StoreId((Long) any(), (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenReturn(new ProductDto());
        PageableResponse<ItemDto> actualItemsByStore = itemServiceImplementation.getItemsByStore(123L, 10, 3);
        assertEquals(1, actualItemsByStore.getData().size());
        assertEquals(1L, actualItemsByStore.getTotalRecords().longValue());
        assertEquals(1, actualItemsByStore.getTotalPages().intValue());
        assertEquals(1, actualItemsByStore.getPageSize().intValue());
        assertEquals(0, actualItemsByStore.getPageNumber().intValue());
        assertTrue(actualItemsByStore.getIsLastPage());
        verify(itemRepository).findAllByStore_StoreId((Long) any(), (Pageable) any());
        verify(itemMini).getProduct();
        verify(itemMini).getPrice();
        verify(itemMini).getItemId();
        verify(itemMini).getItemQuantity();
        verify(itemMini).getExpiryDate();
        verify(itemMini).getManufacturedDate();
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsByStore(Long, Integer, Integer)}
     */
    @Test
    void testGetItemsByStore6() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        ItemMini itemMini = mock(ItemMini.class);
        when(itemMini.getProduct()).thenReturn(product);
        when(itemMini.getPrice()).thenReturn(10.0d);
        when(itemMini.getItemId()).thenReturn(123L);
        when(itemMini.getItemQuantity()).thenReturn(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getExpiryDate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getManufacturedDate())
                .thenReturn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<ItemMini> itemMiniList = new ArrayList<>();
        itemMiniList.add(itemMini);
        PageImpl<ItemMini> pageImpl = new PageImpl<>(itemMiniList);
        when(itemRepository.findAllByStore_StoreId((Long) any(), (Pageable) any())).thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getItemsByStore(123L, 10, 3));
        verify(itemRepository).findAllByStore_StoreId((Long) any(), (Pageable) any());
        verify(itemMini).getProduct();
        verify(itemMini).getItemId();
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsSortedByStore(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetItemsSortedByStore() {
        when(itemRepository.findAllByStore_StoreId((Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        PageableResponse<ItemDto> actualItemsSortedByStore = itemServiceImplementation.getItemsSortedByStore(123L, 10, 3,
                "Keyword", "Order");
        assertTrue(actualItemsSortedByStore.getData().isEmpty());
        assertEquals(0L, actualItemsSortedByStore.getTotalRecords().longValue());
        assertEquals(1, actualItemsSortedByStore.getTotalPages().intValue());
        assertEquals(0, actualItemsSortedByStore.getPageSize().intValue());
        assertEquals(0, actualItemsSortedByStore.getPageNumber().intValue());
        assertTrue(actualItemsSortedByStore.getIsLastPage());
        verify(itemRepository).findAllByStore_StoreId((Long) any(), (Pageable) any());
    }



    /**
     * Method under test: {@link ItemServiceImplementation#getItemsSortedByStore(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetItemsSortedByStore5() {
        when(itemRepository.findAllByStore_StoreId((Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        PageableResponse<ItemDto> actualItemsSortedByStore = itemServiceImplementation.getItemsSortedByStore(123L, 10, 3,
                "Keyword", "asc");
        assertTrue(actualItemsSortedByStore.getData().isEmpty());
        assertEquals(0L, actualItemsSortedByStore.getTotalRecords().longValue());
        assertEquals(1, actualItemsSortedByStore.getTotalPages().intValue());
        assertEquals(0, actualItemsSortedByStore.getPageSize().intValue());
        assertEquals(0, actualItemsSortedByStore.getPageNumber().intValue());
        assertTrue(actualItemsSortedByStore.getIsLastPage());
        verify(itemRepository).findAllByStore_StoreId((Long) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsSortedByStore(Long, Integer, Integer, String, String)}
     */
    @Test
    void testGetItemsSortedByStore6() {
        when(itemRepository.findAllByStore_StoreId((Long) any(), (Pageable) any()))
                .thenThrow(new ResourceException("asc", "asc", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class,
                () -> itemServiceImplementation.getItemsSortedByStore(123L, 10, 3, "Keyword", "Order"));
        verify(itemRepository).findAllByStore_StoreId((Long) any(), (Pageable) any());
    }


    /**
     * Method under test: {@link ItemServiceImplementation#deleteExpired()}
     */
    @Test
    void testDeleteExpired() throws ParseException {
        when(authClient.authenticateUser((LoginRequest) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(itemRepository.findAllByExpiryDateBefore((Date) any())).thenThrow(
                new ResourceException("yyyy-MM-dd", "yyyy-MM-dd", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.deleteExpired());
        verify(itemRepository).findAllByExpiryDateBefore((Date) any());
    }


    /**
     * Method under test: {@link ItemServiceImplementation#getNearlyExpired()}
     */
    @Test
    void testGetNearlyExpired() throws ParseException {
        when(authClient.authenticateUser((LoginRequest) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(itemRepository.findAllByExpiryDateBefore((Date) any())).thenThrow(
                new ResourceException("yyyy-MM-dd", "yyyy-MM-dd", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getNearlyExpired());
        verify(itemRepository).findAllByExpiryDateBefore((Date) any());
    }


    /**
     * Method under test: {@link ItemServiceImplementation#getNearlyExpired()}
     */
    @Test
    void testGetNullInventory() {
        when(authClient.authenticateUser((LoginRequest) any())).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        when(itemRepository.findAllByItemQuantityEquals((Long) any()))
                .thenThrow(new ResourceException("admin1", "admin1", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getNullInventory());
        verify(itemRepository).findAllByItemQuantityEquals((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getNullInventory()}
     */

    @Test
    void testGetNullInventory4() {
        doNothing().when(authClient).logout((String) any());
        when(authClient.authenticateUser((LoginRequest) any()))
                .thenReturn(new ResponseEntity<>(new JwtResponse(), HttpStatus.CONTINUE));
        when(itemRepository.findAllByItemQuantityEquals((Long) any())).thenReturn(new ArrayList<>());
        when(notificationClient.createUserNotification((String) any(), (List<UserNotificationDto>) any()))
                .thenThrow(new ResourceException("admin1", "admin1", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getNullInventory());
        verify(authClient).authenticateUser((LoginRequest) any());
        verify(itemRepository).findAllByItemQuantityEquals((Long) any());
        verify(notificationClient).createUserNotification((String) any(), (List<UserNotificationDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getNullInventory()}
     */
    @Test
    void testGetNullInventory5() {
        JwtResponse jwtResponse = mock(JwtResponse.class);
        when(jwtResponse.getToken()).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        when(jwtResponse.getType()).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        ResponseEntity<JwtResponse> responseEntity = new ResponseEntity<>(jwtResponse, HttpStatus.CONTINUE);

        doNothing().when(authClient).logout((String) any());
        when(authClient.authenticateUser((LoginRequest) any())).thenReturn(responseEntity);
        when(itemRepository.findAllByItemQuantityEquals((Long) any())).thenReturn(new ArrayList<>());
        when(notificationClient.createUserNotification((String) any(), (List<UserNotificationDto>) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getNullInventory());
        verify(authClient).authenticateUser((LoginRequest) any());
        verify(jwtResponse, atLeast(1)).getType();
        verify(itemRepository).findAllByItemQuantityEquals((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getStoreItems(Long)}
     */
    @Test
    void testGetStoreItems() {
        when(itemRepository.findAllByStore_StoreId((Long) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImplementation.getStoreItems(123L).isEmpty());
        verify(itemRepository).findAllByStore_StoreId((Long) any());
    }

    @Test
    void testGetStoreItems3() {
        when(itemRepository.findAllByStore_StoreId((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getStoreItems(123L));
        verify(itemRepository).findAllByStore_StoreId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getStoreItems(Long)}
     */
    @Test
    void testGetStoreItems4() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        ItemMini itemMini = mock(ItemMini.class);
        when(itemMini.getProduct()).thenReturn(product);
        when(itemMini.getPrice()).thenReturn(10.0d);
        when(itemMini.getItemId()).thenReturn(123L);
        when(itemMini.getItemQuantity()).thenReturn(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getExpiryDate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getManufacturedDate())
                .thenReturn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<ItemMini> itemMiniList = new ArrayList<>();
        itemMiniList.add(itemMini);
        when(itemRepository.findAllByStore_StoreId((Long) any())).thenReturn(itemMiniList);
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenReturn(new ProductDto());
        assertEquals(1, itemServiceImplementation.getStoreItems(123L).size());
        verify(itemRepository).findAllByStore_StoreId((Long) any());
        verify(itemMini).getProduct();
        verify(itemMini).getPrice();
        verify(itemMini).getItemId();
        verify(itemMini).getItemQuantity();
        verify(itemMini).getExpiryDate();
        verify(itemMini).getManufacturedDate();
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getStoreItems(Long)}
     */
    @Test
    void testGetStoreItems5() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        ItemMini itemMini = mock(ItemMini.class);
        when(itemMini.getProduct()).thenReturn(product);
        when(itemMini.getPrice()).thenReturn(10.0d);
        when(itemMini.getItemId()).thenReturn(123L);
        when(itemMini.getItemQuantity()).thenReturn(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getExpiryDate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getManufacturedDate())
                .thenReturn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<ItemMini> itemMiniList = new ArrayList<>();
        itemMiniList.add(itemMini);
        when(itemRepository.findAllByStore_StoreId((Long) any())).thenReturn(itemMiniList);
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getStoreItems(123L));
        verify(itemRepository).findAllByStore_StoreId((Long) any());
        verify(itemMini).getProduct();
        verify(itemMini).getItemId();
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#numberOfItemsInStore(Long)}
     */
    @Test
    void testNumberOfItemsInStore() {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        when(itemRepository.countAllByStore_StoreId((Long) any())).thenReturn(completableFuture);
        assertSame(completableFuture, itemServiceImplementation.numberOfItemsInStore(123L));
        verify(itemRepository).countAllByStore_StoreId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#numberOfItemsInStore(Long)}
     */
    @Test
    void testNumberOfItemsInStore2() {
        when(itemRepository.countAllByStore_StoreId((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.numberOfItemsInStore(123L));
        verify(itemRepository).countAllByStore_StoreId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsByStoreToDelete(Long)}
     */
    @Test
    void testGetItemsByStoreToDelete() {
        ArrayList<Item> itemList = new ArrayList<>();
        when(itemRepository.findItemsByStore_StoreId((Long) any())).thenReturn(itemList);
        List<Item> actualItemsByStoreToDelete = itemServiceImplementation.getItemsByStoreToDelete(123L);
        assertSame(itemList, actualItemsByStoreToDelete);
        assertTrue(actualItemsByStoreToDelete.isEmpty());
        verify(itemRepository).findItemsByStore_StoreId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsByStoreToDelete(Long)}
     */
    @Test
    void testGetItemsByStoreToDelete2() {
        when(itemRepository.findItemsByStore_StoreId((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getItemsByStoreToDelete(123L));
        verify(itemRepository).findItemsByStore_StoreId((Long) any());
    }

    @Test
    void getTotalItems() {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        when(itemRepository.count()).thenReturn(5L);
        assertSame(5L, itemServiceImplementation.getTotalItems());
        verify(itemRepository).count();
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsByProductId(Long)}
     */
    @Test
    void testGetItemsByProductId() {
        when(itemRepository.findAllByProduct_ProductId((Long) any())).thenReturn(new ArrayList<>());
        assertTrue(itemServiceImplementation.getItemsByProductId(123L).isEmpty());
        verify(itemRepository).findAllByProduct_ProductId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsByProductId(Long)}
     */
    @Test
    void testGetItemsByProductId2() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);

        ArrayList<Item> itemList = new ArrayList<>();
        itemList.add(item);
        when(itemRepository.findAllByProduct_ProductId((Long) any())).thenReturn(itemList);
        List<Long> actualItemsByProductId = itemServiceImplementation.getItemsByProductId(123L);
        assertEquals(1, actualItemsByProductId.size());
        assertEquals(123L, actualItemsByProductId.get(0));
        verify(itemRepository).findAllByProduct_ProductId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getItemsByProductId(Long)}
     */
    @Test
    void testGetItemsByProductId3() {
        when(itemRepository.findAllByProduct_ProductId((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getItemsByProductId(123L));
        verify(itemRepository).findAllByProduct_ProductId((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#createItem(Long, ItemDto)}
     */
    @Test
    void testCreateItem() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item);
        when(storeService.getStore((Long) any())).thenReturn(new StoreDto());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.createItem(123L, new ItemDto()));
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
        verify(storeService).getStore((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#createItem(Long, ItemDto)}
     */
    @Test
    void testCreateItem2() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
//        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
//        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item);
        when(storeService.getStore((Long) any()))
                .thenThrow(new ResourceException("Item", "Item", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.createItem(123L, new ItemDto()));
//        verify(modelMapper).map((Object) any(), (Class<Object>) any());
        verify(storeService).getStore((Long) any());
    }

    @Test
    void testCreateItem6() {
        when(storeService.getStore((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.createItem("Jwt", 123L, new ItemDto()));
        verify(storeService).getStore((Long) any());
    }

    @Test
    void testCreateItem7() {
        when(modelMapper.map(any(),any())).thenReturn(mockItem).thenReturn(mockItemDto);
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);
        doNothing().when(productService).updateQuantity(anyLong(),anyLong());
        itemServiceImplementation.createItem(1L,mockItemDto);
        verify(modelMapper,times(2)).map(any(),any());
        verify(storeService,times(1)).getStore(anyLong());
    }

    @Test
    void testCreateItem8() {
        when(JwtUtils.verifyId(anyString(),anyLong(),anyBoolean())).thenReturn(false);
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);
        assertThrows(ResponseStatusException.class, () -> itemServiceImplementation.createItem("Bearer efddse", 123L,mockItemDto));
    }

    @Test
    void testCreateItem9() {
        when(JwtUtils.verifyId(anyString(),anyLong(),anyBoolean())).thenReturn(true);
        when(storeService.getStore(anyLong())).thenReturn(mockStoreDto);
        when(modelMapper.map(any(),any())).thenReturn(mockItem).thenReturn(mockItemDto);
        when(itemRepository.save(any())).thenReturn(mockItem);
        ItemDto result = itemServiceImplementation.createItem("Bearer asdasd", 123L, mockItemDto);
        verify(storeService,times(2)).getStore(anyLong());
    }

    @Test
    void testCreateItems() {
        when(itemRepository.saveAll((Iterable<Item>) any())).thenReturn(new ArrayList<>());
        itemServiceImplementation.createItems(new ArrayList<>());
        verify(itemRepository).saveAll((Iterable<Item>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#createItems(List)}
     */
    @Test
    void testCreateItems2() {
        when(itemRepository.saveAll((Iterable<Item>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.createItems(new ArrayList<>()));
        verify(itemRepository).saveAll((Iterable<Item>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#createItems(List)}
     */
    @Test
    void testCreateItems3() {
        when(itemRepository.saveAll((Iterable<Item>) any())).thenThrow(new ResponseStatusException(HttpStatus.CONTINUE));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.createItems(new ArrayList<>()));
        verify(itemRepository).saveAll((Iterable<Item>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#updateItem(ItemDto)}
     */
    @Test
    void testUpdateItem() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode(1);
        address1.setState("MD");
        address1.setStreet("Street");

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));

        Store store1 = new Store();
        store1.setAddress(address1);
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store1.setCreatedDate(Date.from(atStartOfDayResult7.atZone(ZoneId.of("UTC")).toInstant()));
        store1.setManager(manager1);
        store1.setRevenue(10.0d);
        store1.setStoreId(123L);
        store1.setStoreName("Store Name");

        Item item1 = new Item();
        LocalDateTime atStartOfDayResult8 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item1.setExpiryDate(Date.from(atStartOfDayResult8.atZone(ZoneId.of("UTC")).toInstant()));
        item1.setItemId(123L);
        item1.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult9 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item1.setManufacturedDate(Date.from(atStartOfDayResult9.atZone(ZoneId.of("UTC")).toInstant()));
        item1.setPrice(10.0d);
        item1.setProduct(product1);
        item1.setStore(store1);
        when(itemRepository.save((Item) any())).thenReturn(item1);
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);

        Product product2 = new Product();
        product2.setCategorySet(new HashSet<>());
        product2.setDescription("The characteristics of someone or something");
        product2.setDosageForm("Dosage Form");
        product2.setImageUrl("https://example.org/example");
        product2.setProductId(123L);
        product2.setProductName("Product Name");
        product2.setProductType(true);
        product2.setProprietaryName("Proprietary Name");
        product2.setQuantity(1L);

        Address address2 = new Address();
        address2.setAddressId(123L);
        address2.setCity("Oxford");
        address2.setCountry("GB");
        address2.setPinCode(1);
        address2.setState("MD");
        address2.setStreet("Street");

        Manager manager2 = new Manager();
        manager2.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult10 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager2.setLastModified(Date.from(atStartOfDayResult10.atZone(ZoneId.of("UTC")).toInstant()));
        manager2.setLicenseNo("License No");
        manager2.setManagerId(123L);
        manager2.setName("Name");
        manager2.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult11 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager2.setRegistrationDate(Date.from(atStartOfDayResult11.atZone(ZoneId.of("UTC")).toInstant()));

        Store store2 = new Store();
        store2.setAddress(address2);
        LocalDateTime atStartOfDayResult12 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store2.setCreatedDate(Date.from(atStartOfDayResult12.atZone(ZoneId.of("UTC")).toInstant()));
        store2.setManager(manager2);
        store2.setRevenue(10.0d);
        store2.setStoreId(123L);
        store2.setStoreName("Store Name");

        Item item2 = new Item();
        LocalDateTime atStartOfDayResult13 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item2.setExpiryDate(Date.from(atStartOfDayResult13.atZone(ZoneId.of("UTC")).toInstant()));
        item2.setItemId(123L);
        item2.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult14 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item2.setManufacturedDate(Date.from(atStartOfDayResult14.atZone(ZoneId.of("UTC")).toInstant()));
        item2.setPrice(10.0d);
        item2.setProduct(product2);
        item2.setStore(store2);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item2);
        assertThrows(ResourceException.class, () -> itemServiceImplementation.updateItem(new ItemDto()));
        verify(itemRepository).save((Item) any());
        verify(itemRepository).findById((Long) any());
        verify(modelMapper, atLeast(1)).map((Object) any(), (Class<Object>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#updateItem(ItemDto)}
     */
    @Test
    void testUpdateItem2() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.save((Item) any()))
                .thenThrow(new ResourceException("Items", "Items", "Field Value", ResourceException.ErrorType.CREATED));
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);

        Address address1 = new Address();
        address1.setAddressId(123L);
        address1.setCity("Oxford");
        address1.setCountry("GB");
        address1.setPinCode(1);
        address1.setState("MD");
        address1.setStreet("Street");

        Manager manager1 = new Manager();
        manager1.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult5 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setLastModified(Date.from(atStartOfDayResult5.atZone(ZoneId.of("UTC")).toInstant()));
        manager1.setLicenseNo("License No");
        manager1.setManagerId(123L);
        manager1.setName("Name");
        manager1.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult6 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager1.setRegistrationDate(Date.from(atStartOfDayResult6.atZone(ZoneId.of("UTC")).toInstant()));

        Store store1 = new Store();
        store1.setAddress(address1);
        LocalDateTime atStartOfDayResult7 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store1.setCreatedDate(Date.from(atStartOfDayResult7.atZone(ZoneId.of("UTC")).toInstant()));
        store1.setManager(manager1);
        store1.setRevenue(10.0d);
        store1.setStoreId(123L);
        store1.setStoreName("Store Name");

        Item item1 = new Item();
        LocalDateTime atStartOfDayResult8 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item1.setExpiryDate(Date.from(atStartOfDayResult8.atZone(ZoneId.of("UTC")).toInstant()));
        item1.setItemId(123L);
        item1.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult9 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item1.setManufacturedDate(Date.from(atStartOfDayResult9.atZone(ZoneId.of("UTC")).toInstant()));
        item1.setPrice(10.0d);
        item1.setProduct(product1);
        item1.setStore(store1);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item1);
        assertThrows(ResourceException.class, () -> itemServiceImplementation.updateItem(new ItemDto()));
        verify(itemRepository).save((Item) any());
        verify(itemRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void testUpdateItem8() {
        when(itemRepository.saveAll((Iterable<Item>) any())).thenReturn(new ArrayList<>());
        itemServiceImplementation.updateItem(new ArrayList<>());
        verify(itemRepository).saveAll((Iterable<Item>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#updateItem(List)}
     */
    @Test
    void testUpdateItem9() {
        when(itemRepository.saveAll((Iterable<Item>) any())).thenReturn(new ArrayList<>());

        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        when(modelMapper.map((Object) any(), (Class<Item>) any())).thenReturn(item);

        ArrayList<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(new ItemDto());
        itemServiceImplementation.updateItem(itemDtoList);
        verify(itemRepository).saveAll((Iterable<Item>) any());
        verify(modelMapper).map((Object) any(), (Class<Item>) any());
    }

    @Test
    void updateItem10() {
        // catch scenario in update item function - void update dto lists
        ArrayList<ItemDto> itemDtoList = new ArrayList<>();
        itemDtoList.add(mockItemDto);
        when(modelMapper.map(any(),any())).thenReturn(mockItem);
        when(itemRepository.saveAll(any())).thenThrow(new EntityNotFoundException());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.updateItem(itemDtoList));
    }

    @Test
    void updateItem11() {
        // ItemDto updateItem() - if empty condition
        when(modelMapper.map(any(),any())).thenReturn(mockItem);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.updateItem(mockItemDto));
    }

    @Test
    void updateItem12() {
        // ItemDto updateItem() - if empty condition
        when(modelMapper.map(any(),any())).thenReturn(mockItem).thenReturn(mockItemDto);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(mockItem));
        when(itemRepository.save(any())).thenReturn(mockItem);
        ItemDto resultItem = itemServiceImplementation.updateItem(mockItemDto);
        assertEquals(mockItem.getItemId(),resultItem.getItemId());
        verify(modelMapper,times(2)).map(any(),any());
    }

    @Test
    void updateItem13() {
        // updateItem(jwt, itemDto) - catch exception
        when(JwtUtils.verifyId(anyString(),anyLong(),anyBoolean())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> itemServiceImplementation.updateItem("Bearer efddsebdhedbhjvfhj", mockItemDto));
    }

    @Test
    void updateItem14() {
        // updateItem(jwt, itemDto) - catch exception
        when(itemRepository.findById((Long) any())).thenReturn(Optional.of(mockItem));
        when(modelMapper.map((Object) any(),any())).thenReturn(mockItem).thenReturn(mockItemDto);
        when(JwtUtils.verifyId(anyString(),anyLong(),anyBoolean())).thenReturn(true);
        when(itemRepository.save(any())).thenReturn(mockItem);
        itemServiceImplementation.updateItem("Bearer asdasadnvc", mockItemDto);
        verify(modelMapper,times(2)).map(any(),any());
        verify(itemRepository,times(1)).save(any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#deleteItem(Long)}
     */
    @Test
    void testDeleteItem() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);
        doNothing().when(itemRepository).deleteById((Long) any());
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        doNothing().when(productService).updateQuantity((Long) any(), (Long) any());
        itemServiceImplementation.deleteItem(123L);
        verify(itemRepository).findById((Long) any());
        verify(itemRepository).deleteById((Long) any());
        verify(productService).updateQuantity((Long) any(), (Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#deleteItem(Long)}
     */
    @Test
    void testDeleteItem2() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);
        doNothing().when(itemRepository).deleteById((Long) any());
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(productService)
                .updateQuantity((Long) any(), (Long) any());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.deleteItem(123L));
        verify(itemRepository).findById((Long) any());
        verify(productService).updateQuantity((Long) any(), (Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#deleteItem(Long)}
     */
    @Test
    void testDeleteItem3() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);
        doNothing().when(itemRepository).deleteById((Long) any());
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        doThrow(new ResponseStatusException(HttpStatus.CONTINUE)).when(productService)
                .updateQuantity((Long) any(), (Long) any());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.deleteItem(123L));
        verify(itemRepository).findById((Long) any());
        verify(productService).updateQuantity((Long) any(), (Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#deleteItem(Long)}
     */
    @Test
    void testDeleteItem4() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Product product1 = new Product();
        product1.setCategorySet(new HashSet<>());
        product1.setDescription("The characteristics of someone or something");
        product1.setDosageForm("Dosage Form");
        product1.setImageUrl("https://example.org/example");
        product1.setProductId(123L);
        product1.setProductName("Product Name");
        product1.setProductType(true);
        product1.setProprietaryName("Proprietary Name");
        product1.setQuantity(1L);
        Item item = mock(Item.class);
        when(item.getItemQuantity()).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        when(item.getProduct()).thenReturn(product1);
        doNothing().when(item).setExpiryDate((Date) any());
        doNothing().when(item).setItemId((Long) any());
        doNothing().when(item).setItemQuantity((Long) any());
        doNothing().when(item).setManufacturedDate((Date) any());
        doNothing().when(item).setPrice((Double) any());
        doNothing().when(item).setProduct((Product) any());
        doNothing().when(item).setStore((Store) any());
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);
        doNothing().when(itemRepository).deleteById((Long) any());
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        doNothing().when(productService).updateQuantity((Long) any(), (Long) any());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.deleteItem(123L));
        verify(itemRepository).findById((Long) any());
        verify(item).getProduct();
        verify(item).getItemQuantity();
        verify(item).setExpiryDate((Date) any());
        verify(item).setItemId((Long) any());
        verify(item).setItemQuantity((Long) any());
        verify(item).setManufacturedDate((Date) any());
        verify(item).setPrice((Double) any());
        verify(item).setProduct((Product) any());
        verify(item).setStore((Store) any());
    }


    @Test
    void testDeleteItem6() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);

        Address address = new Address();
        address.setAddressId(123L);
        address.setCity("Oxford");
        address.setCountry("GB");
        address.setPinCode(1);
        address.setState("MD");
        address.setStreet("Street");

        Manager manager = new Manager();
        manager.setApprovalStatus(ApprovalStatus.APPROVED);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setLastModified(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        manager.setLicenseNo("License No");
        manager.setManagerId(123L);
        manager.setName("Name");
        manager.setPhoneNo("4105551212");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        manager.setRegistrationDate(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        Store store = new Store();
        store.setAddress(address);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        store.setCreatedDate(Date.from(atStartOfDayResult2.atZone(ZoneId.of("UTC")).toInstant()));
        store.setManager(manager);
        store.setRevenue(10.0d);
        store.setStoreId(123L);
        store.setStoreName("Store Name");

        Item item = new Item();
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setExpiryDate(Date.from(atStartOfDayResult3.atZone(ZoneId.of("UTC")).toInstant()));
        item.setItemId(123L);
        item.setItemQuantity(42L);
        LocalDateTime atStartOfDayResult4 = LocalDate.of(1970, 1, 1).atStartOfDay();
        item.setManufacturedDate(Date.from(atStartOfDayResult4.atZone(ZoneId.of("UTC")).toInstant()));
        item.setPrice(10.0d);
        item.setProduct(product);
        item.setStore(store);
        Optional<Item> ofResult = Optional.of(item);
        when(itemRepository.findById((Long) any())).thenReturn(ofResult);
        when(modelMapper.map((Object) any(), (Class<ItemDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.deleteItem("Jwt", 123L));
        verify(itemRepository).findById((Long) any());
        verify(modelMapper).map((Object) any(), (Class<ItemDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#deleteItem(String, Long)}
     */
    @Test
    void testDeleteItem7() {
        when(itemRepository.findById((Long) any())).thenReturn(Optional.empty());
//        when(modelMapper.map((Object) any(), (Class<Object>) any())).thenReturn("Map");
//        when(modelMapper.map((Object) any(), (Class<ItemDto>) any())).thenReturn(new ItemDto());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.deleteItem("Jwt", 123L));
        verify(itemRepository).findById((Long) any());
//        verify(modelMapper).map((Object) any(), (Class<Object>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#deleteItems(List)}
     */
    @Test
    void testDeleteItems() {
        doNothing().when(itemRepository).deleteAllById((Iterable<Long>) any());
        itemServiceImplementation.deleteItems(new ArrayList<>());
        verify(itemRepository).deleteAllById((Iterable<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#deleteItems(List)}
     */
    @Test
    void testDeleteItems2() {
        doThrow(new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED))
                .when(itemRepository)
                .deleteAllById((Iterable<Long>) any());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.deleteItems(new ArrayList<>()));
        verify(itemRepository).deleteAllById((Iterable<Long>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getSortedItemsByProductId(Integer, Integer, Long)}
     */
    @Test
    void testGetSortedItemsByProductId() {
        when(itemRepository.findAllByProduct_ProductIdOrderByPriceAsc((Long) any(), (Pageable) any()))
                .thenReturn(new PageImpl<>(new ArrayList<>()));
        PageableResponse<ItemDto> actualSortedItemsByProductId = itemServiceImplementation.getSortedItemsByProductId(10,
                3, 123L);
        assertTrue(actualSortedItemsByProductId.getData().isEmpty());
        assertEquals(0L, actualSortedItemsByProductId.getTotalRecords().longValue());
        assertEquals(1, actualSortedItemsByProductId.getTotalPages().intValue());
        assertEquals(0, actualSortedItemsByProductId.getPageSize().intValue());
        assertEquals(0, actualSortedItemsByProductId.getPageNumber().intValue());
        assertTrue(actualSortedItemsByProductId.getIsLastPage());
        verify(itemRepository).findAllByProduct_ProductIdOrderByPriceAsc((Long) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getSortedItemsByProductId(Integer, Integer, Long)}
     */
    @Test
    void testGetSortedItemsByProductId4() {
        when(itemRepository.findAllByProduct_ProductIdOrderByPriceAsc((Long) any(), (Pageable) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getSortedItemsByProductId(10, 3, 123L));
        verify(itemRepository).findAllByProduct_ProductIdOrderByPriceAsc((Long) any(), (Pageable) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getSortedItemsByProductId(Integer, Integer, Long)}
     */
    @Test
    void testGetSortedItemsByProductId5() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        ItemMini itemMini = mock(ItemMini.class);
        when(itemMini.getProduct()).thenReturn(product);
        when(itemMini.getPrice()).thenReturn(10.0d);
        when(itemMini.getItemId()).thenReturn(123L);
        when(itemMini.getItemQuantity()).thenReturn(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getExpiryDate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getManufacturedDate())
                .thenReturn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<ItemMini> itemMiniList = new ArrayList<>();
        itemMiniList.add(itemMini);
        PageImpl<ItemMini> pageImpl = new PageImpl<>(itemMiniList);
        when(itemRepository.findAllByProduct_ProductIdOrderByPriceAsc((Long) any(), (Pageable) any()))
                .thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenReturn(new ProductDto());
        PageableResponse<ItemDto> actualSortedItemsByProductId = itemServiceImplementation.getSortedItemsByProductId(10,
                3, 123L);
        assertEquals(1, actualSortedItemsByProductId.getData().size());
        assertEquals(1L, actualSortedItemsByProductId.getTotalRecords().longValue());
        assertEquals(1, actualSortedItemsByProductId.getTotalPages().intValue());
        assertEquals(1, actualSortedItemsByProductId.getPageSize().intValue());
        assertEquals(0, actualSortedItemsByProductId.getPageNumber().intValue());
        assertTrue(actualSortedItemsByProductId.getIsLastPage());
        verify(itemRepository).findAllByProduct_ProductIdOrderByPriceAsc((Long) any(), (Pageable) any());
        verify(itemMini).getProduct();
        verify(itemMini).getPrice();
        verify(itemMini).getItemId();
        verify(itemMini).getItemQuantity();
        verify(itemMini).getExpiryDate();
        verify(itemMini).getManufacturedDate();
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#getSortedItemsByProductId(Integer, Integer, Long)}
     */
    @Test
    void testGetSortedItemsByProductId6() {
        Product product = new Product();
        product.setCategorySet(new HashSet<>());
        product.setDescription("The characteristics of someone or something");
        product.setDosageForm("Dosage Form");
        product.setImageUrl("https://example.org/example");
        product.setProductId(123L);
        product.setProductName("Product Name");
        product.setProductType(true);
        product.setProprietaryName("Proprietary Name");
        product.setQuantity(1L);
        ItemMini itemMini = mock(ItemMini.class);
        when(itemMini.getProduct()).thenReturn(product);
        when(itemMini.getPrice()).thenReturn(10.0d);
        when(itemMini.getItemId()).thenReturn(123L);
        when(itemMini.getItemQuantity()).thenReturn(42L);
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getExpiryDate()).thenReturn(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        when(itemMini.getManufacturedDate())
                .thenReturn(Date.from(atStartOfDayResult1.atZone(ZoneId.of("UTC")).toInstant()));

        ArrayList<ItemMini> itemMiniList = new ArrayList<>();
        itemMiniList.add(itemMini);
        PageImpl<ItemMini> pageImpl = new PageImpl<>(itemMiniList);
        when(itemRepository.findAllByProduct_ProductIdOrderByPriceAsc((Long) any(), (Pageable) any()))
                .thenReturn(pageImpl);
        when(modelMapper.map((Object) any(), (Class<ProductDto>) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.getSortedItemsByProductId(10, 3, 123L));
        verify(itemRepository).findAllByProduct_ProductIdOrderByPriceAsc((Long) any(), (Pageable) any());
        verify(itemMini).getProduct();
        verify(itemMini).getItemId();
        verify(modelMapper).map((Object) any(), (Class<ProductDto>) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists() {
        when(itemRepository.existsById((Long) any())).thenReturn(true);
        assertTrue(itemServiceImplementation.checkIfExists(123L));
        verify(itemRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists2() {
        when(itemRepository.existsById((Long) any())).thenReturn(false);
        assertFalse(itemServiceImplementation.checkIfExists(123L));
        verify(itemRepository).existsById((Long) any());
    }

    /**
     * Method under test: {@link ItemServiceImplementation#checkIfExists(Long)}
     */
    @Test
    void testCheckIfExists3() {
        when(itemRepository.existsById((Long) any())).thenThrow(
                new ResourceException("Resource Name", "Field Name", "Field Value", ResourceException.ErrorType.CREATED));
        assertThrows(ResourceException.class, () -> itemServiceImplementation.checkIfExists(123L));
        verify(itemRepository).existsById((Long) any());
    }

    @Test
    void testDeleteItem8() {
        when(itemRepository.findById((Long) any())).thenReturn(Optional.of(mockItem));
        when(modelMapper.map((Object) any(), (Class<ItemDto>) any())).thenReturn(mockItemDto);
        when(JwtUtils.verifyId(anyString(),anyLong(),anyBoolean())).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> itemServiceImplementation.deleteItem("Bearer efddse", 123L));
    }

    @Test
    void testDeleteItem9() {
        Optional<Item> optionalItem = Optional.of(mockItem);
        when(JwtUtils.verifyId(anyString(),anyLong(),anyBoolean())).thenReturn(true);
        when(itemRepository.findById((Long) any())).thenReturn(optionalItem);
        when(modelMapper.map((Object) any(), (Class<ItemDto>) any())).thenReturn(mockItemDto);
        doNothing().when(productService).updateQuantity(anyLong(),anyLong());
        doNothing().when(itemRepository).deleteById(any());
        itemServiceImplementation.deleteItem("Bearer eaysduyaskd",123L);
        verify(productService, times(1)).updateQuantity(anyLong(),anyLong());
    }

    @Test
    void testDeleteItem10() {
        when(itemRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResourceException.class, () -> itemServiceImplementation.deleteItem(123L));
    }
}

