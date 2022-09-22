package com.pharmacy.service.service.implementation;

import com.pharmacy.service.client.auth.AuthClient;
import com.pharmacy.service.client.notify.NotificationClient;
import com.pharmacy.service.dto.ItemDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.ProductDto;
import com.pharmacy.service.dtoexternal.NotificationSeverity;
import com.pharmacy.service.dtoexternal.NotificationStatus;
import com.pharmacy.service.dtoexternal.UserNotificationDto;
import com.pharmacy.service.exception.ResourceException;
import com.pharmacy.service.model.Item;
import com.pharmacy.service.repository.ItemRepository;
import com.pharmacy.service.service.ItemService;
import com.pharmacy.service.service.ProductService;
import com.pharmacy.service.service.StoreService;
import com.pharmacy.service.utils.ItemMini;
import com.pharmacy.service.utils.JwtUtils;
import com.pharmacy.service.utils.SchedulerTasks;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ItemServiceImplementation implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private AuthClient authClient;

    @Override
    public ItemDto getItem(Long itemId) {
        Optional<Item> optionalItem = itemRepository.findById(itemId);
        if (optionalItem.isEmpty())
            throw new ResourceException("Item", "ID", itemId, ResourceException.ErrorType.FOUND);
        Item item = optionalItem.get();
        return modelMapper.map(item, ItemDto.class);
    }

    @Override
    public PageableResponse<ItemDto> getItemsWithoutStoreInfo(Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        return getPageableItemResponse(itemRepository.findBy(requestedPage));
    }

    private PageableResponse<ItemDto> getPageableItemResponse(Page<ItemMini> itemMiniPage) {
        Page<ItemDto> itemDtoPage = itemMiniPage.map(itemMini -> new ItemDto(itemMini.getItemId(),
                modelMapper.map(itemMini.getProduct(), ProductDto.class), itemMini.getItemQuantity(),
                itemMini.getPrice(), itemMini.getManufacturedDate(), itemMini.getExpiryDate()));
        List<ItemDto> itemDtoList = itemDtoPage.getContent();
        PageableResponse<ItemDto> pageableItemResponse = new PageableResponse<>();
        return pageableItemResponse.setResponseData(itemDtoList, itemDtoPage);
    }

    @Override
    public PageableResponse<ItemDto> getItemsByStore(Long storeId, Integer pageNumber, Integer pageSize) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<ItemMini> itemMiniPage = itemRepository.findAllByStore_StoreId(storeId, requestedPage);
        return getPageableItemResponse(itemMiniPage);
    }

    @Override
    public PageableResponse<ItemDto> getItemsSortedByStore(Long storeId, Integer pageNumber, Integer pageSize,
                                                           String keyword, String order) {
        Pageable requestedPage;
        if (order.equalsIgnoreCase("asc"))
            requestedPage = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, keyword));
        else
            requestedPage = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, keyword));
        Page<ItemMini> itemMiniPage = itemRepository.findAllByStore_StoreId(storeId, requestedPage);
        return getPageableItemResponse(itemMiniPage);
    }

    @Override
    public Long getTotalItems() {
        return itemRepository.count();
    }

    private List<Item> getByDateAndNotify(Date date) {
        List<Item> expiredItemList = itemRepository.findAllByExpiryDateBefore(date);
        boolean nearExpired = date.after(new Date());
        List<UserNotificationDto> notifyList = new ArrayList<>();
        for (Item item : expiredItemList) {
            UserNotificationDto userNotificationDto = new UserNotificationDto();
            userNotificationDto.setUserId(item.getStore().getManager().getManagerId());
            userNotificationDto.setMessage((nearExpired ? "Nearly" : "Expired") + " items in Inventory!");
            userNotificationDto.setDescription(
                    item.getProduct().getProductName() + " with quantity " + item.getItemQuantity() + (nearExpired ? " will expire in 3 days." : " has expired."));
            userNotificationDto.setStatus(NotificationStatus.UNSEEN);
            userNotificationDto.setSeverity(NotificationSeverity.WARNING);
            userNotificationDto.setCreatedOn(new Date());
            notifyList.add(userNotificationDto);
        }
        SchedulerTasks schedulerTasks = new SchedulerTasks(authClient, notificationClient, "admin1", "admin1");
        schedulerTasks.sendScheduledNotification(notifyList);
        schedulerTasks.logout();
        return expiredItemList;
    }

    @Override
    public void deleteExpired() throws ParseException {
        List<Item> expiredItemList = getByDateAndNotify(
                new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().toString()));
        try {
            itemRepository.deleteAll(expiredItemList);
        } catch (Exception e) {
            throw new ResourceException("Item", "expired items", expiredItemList, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public void getNearlyExpired() throws ParseException {
        getByDateAndNotify(new SimpleDateFormat("yyyy-MM-dd").parse(LocalDate.now().plusDays(3L).toString()));
    }

    @Override
    public void getNullInventory() {
        List<Item> zeroInventoryItemList = itemRepository.findAllByItemQuantityEquals(0L);
        List<UserNotificationDto> notifyList = new ArrayList<>();
        for (Item i : zeroInventoryItemList) {
            UserNotificationDto userNotificationDto = new UserNotificationDto();
            userNotificationDto.setUserId(i.getStore().getManager().getManagerId());
            userNotificationDto.setMessage("No units left in inventory!");
            userNotificationDto
                    .setDescription(i.getProduct().getProductName() + " has zero units left in your inventory.");
            userNotificationDto.setStatus(NotificationStatus.UNSEEN);
            userNotificationDto.setSeverity(NotificationSeverity.WARNING);
            userNotificationDto.setCreatedOn(new Date());
            notifyList.add(userNotificationDto);
        }
        try {
            SchedulerTasks schedulerTasks = new SchedulerTasks(authClient, notificationClient, "admin1", "admin1");
            schedulerTasks.sendScheduledNotification(notifyList);
            schedulerTasks.logout();
        } catch (Exception e) {
            throw new ResourceException("Item", "expired items", zeroInventoryItemList,
                    ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public List<ItemDto> getStoreItems(Long storeId) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        for (ItemMini itemMini : itemRepository.findAllByStore_StoreId(storeId))
            itemDtoList.add(new ItemDto(itemMini.getItemId(), modelMapper.map(itemMini.getProduct(), ProductDto.class),
                    itemMini.getItemQuantity(), itemMini.getPrice(), itemMini.getManufacturedDate(),
                    itemMini.getExpiryDate()));
        return itemDtoList;
    }

    @Override
    public CompletableFuture<Integer> numberOfItemsInStore(Long storeId) {
        return itemRepository.countAllByStore_StoreId(storeId);
    }

    @Override
    public List<Item> getItemsByStoreToDelete(Long storeId) {
        return itemRepository.findItemsByStore_StoreId(storeId);
    }

    @Override
    public List<Long> getItemsByProductId(Long productId) {
        List<Long> itemIdList = new ArrayList<>();
        for (Item item : itemRepository.findAllByProduct_ProductId(productId))
            itemIdList.add(item.getItemId());
        return itemIdList;
    }

    @Override
    public ItemDto createItem(Long storeId, ItemDto itemDto) {
        try {
            itemDto.setStore(storeService.getStore(storeId));
            Item item = modelMapper.map(itemDto, Item.class);
            productService.updateQuantity(itemDto.getProduct().getProductId(), item.getItemQuantity());
            return modelMapper.map(itemRepository.save(item), ItemDto.class);
        } catch (Exception e) {
            throw new ResourceException("Item", "item", itemDto, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public ItemDto createItem(String jwt, Long storeId, ItemDto itemDto) {
        if (JwtUtils.verifyId(jwt, storeService.getStore(storeId).getManager().getManagerId(), false))
            return createItem(storeId, itemDto);
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only add items to their own stores");
    }

    @Override
    @Async("asyncExecutor")
    public CompletableFuture<Boolean> createItems(List<Item> itemList) {
        try {
            itemRepository.saveAll(itemList);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            throw new ResourceException("Items", "item list", itemList, ResourceException.ErrorType.CREATED, e);
        }
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto) {
        Item newItem = modelMapper.map(itemDto, Item.class);
        Optional<Item> optionalItem = itemRepository.findById(newItem.getItemId());
        if (optionalItem.isEmpty())
            throw new ResourceException("Item", "item", itemDto, ResourceException.ErrorType.FOUND);
        try {
            return modelMapper.map(itemRepository.save(newItem), ItemDto.class);
        } catch (Exception e) {
            throw new ResourceException("Items", "item", itemDto, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public ItemDto updateItem(String jwt, ItemDto itemDto) {
        if (JwtUtils.verifyId(jwt, itemDto.getStore().getManager().getManagerId(), false))
            return updateItem(itemDto);
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                "Managers can only update items of their own stores");
    }

    @Override
    public void updateItem(List<ItemDto> itemDtoList) {
        List<Item> itemList = new ArrayList<>();
        for (ItemDto itemDto : itemDtoList)
            itemList.add(modelMapper.map(itemDto, Item.class));
        try {
            itemRepository.saveAll(itemList);
        } catch (Exception e) {
            throw new ResourceException("Items", "item  list", itemList, ResourceException.ErrorType.UPDATED, e);
        }
    }

    @Override
    public void deleteItem(Long itemId) {
        try {
            Optional<Item> optionalItem = itemRepository.findById(itemId);
            if (optionalItem.isEmpty())
                throw new ResourceException("Item", "ID", itemId, ResourceException.ErrorType.FOUND);
            Item item = optionalItem.get();
            productService.updateQuantity(item.getProduct().getProductId(), -item.getItemQuantity());
            itemRepository.deleteById(itemId);
        } catch (Exception e) {
            throw new ResourceException("Item", "ID", itemId, ResourceException.ErrorType.DELETED, e);
        }
    }

    @Override
    public void deleteItem(String jwt, Long itemId) {
        if (JwtUtils.verifyId(jwt, getItem(itemId).getStore().getManager().getManagerId(), false))
            deleteItem(itemId);
        else
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Managers can only delete items of their own stores");
    }

    @Override
    public void deleteItems(List<Long> itemIdList) {
        itemRepository.deleteAllById(itemIdList);
    }

    @Override
    public PageableResponse<ItemDto> getSortedItemsByProductId(Integer pageNumber, Integer pageSize, Long productId) {
        Pageable requestedPage = PageRequest.of(pageNumber, pageSize);
        Page<ItemMini> itemMiniPage = itemRepository.findAllByProduct_ProductIdOrderByPriceAsc(productId,
                requestedPage);
        return getPageableItemResponse(itemMiniPage);
    }

    @Override
    public boolean checkIfExists(Long categoryId) {
        return itemRepository.existsById(categoryId);
    }

}
