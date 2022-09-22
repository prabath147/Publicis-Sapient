package com.pharmacy.service.controller;

import com.pharmacy.service.client.notify.NotificationClient;
import com.pharmacy.service.dto.ItemDto;
import com.pharmacy.service.dto.PageableResponse;
import com.pharmacy.service.dto.StoreDto;
import com.pharmacy.service.dtoexternal.*;
import com.pharmacy.service.model.Store;
import com.pharmacy.service.service.*;
import com.pharmacy.service.utils.JwtUtils;
import com.pharmacy.service.utils.ReadInventoryFromExcel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pharmacy/store")
@RequiredArgsConstructor
public class StoreController {

    @Autowired
    private NotificationClient notificationClient;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private StoreService storeService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ManagerService managerService;

    @Autowired
    private ProductService productService;


    @GetMapping(value = "/get-store")
    public ResponseEntity<PageableResponse<StoreDto>> getStores(@RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        return new ResponseEntity<>(storeService.getStores(pageNumber, pageSize), HttpStatus.OK);
    }

    @GetMapping(value = "/get-store/{id}")
    public ResponseEntity<StoreDto> getStoreById(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long storeId) {
        StoreDto storeDto = storeService.getStore(storeId);
        if (JwtUtils.verifyId(jwt, storeDto.getManager().getManagerId(), false))
            return ResponseEntity.status(HttpStatus.OK).body(storeDto);
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see their own store!");
    }

    // NEW
    @GetMapping(value = "/total-stores/{id}")
    public ResponseEntity<Long> totalNoOfStores(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long managerId) {
        if (!JwtUtils.verifyId(jwt, managerId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see their own stores!");
        return ResponseEntity.ok().body(storeService.noOfStores(managerId));
    }

    // NEW
    @GetMapping(value = "/total-stores")
    public ResponseEntity<Long> totalNoOfStores() {
        return ResponseEntity.ok().body(storeService.noOfStores());
    }

    @GetMapping(value = "/total-revenue/{id}")
    public ResponseEntity<Double> totalRevenue(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long managerId) {
        if (!JwtUtils.verifyId(jwt, managerId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see their own stores!");
        return ResponseEntity.ok().body(storeService.totalRevenue(managerId));
    }

    // NEW
    @GetMapping("/get-manager-stores/{id}")
    public ResponseEntity<PageableResponse<StoreDto>> getManagerStores(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long managerId, @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        if (JwtUtils.verifyId(jwt, managerId, false))
            return ResponseEntity.status(HttpStatus.OK).body(storeService.getManagerStore(managerId, pageNumber, pageSize));
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see their own stores!");
    }

    @GetMapping(value = "/get-store-items/{id}")
    public ResponseEntity<PageableResponse<ItemDto>> getStoreItems(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long storeId, @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        StoreDto storeDto = storeService.getStore(storeId);
        if (JwtUtils.verifyId(jwt, storeDto.getManager().getManagerId(), false))
            return ResponseEntity.status(HttpStatus.OK).body(itemService.getItemsByStore(storeId, pageNumber, pageSize));
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see items of their own stores!");
    }

    @GetMapping(value = "/get-store-items-sorted/{id}")
    public ResponseEntity<PageableResponse<ItemDto>> getStoreItemsSorted(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long storeId, @RequestParam(value = "pageNumber", defaultValue = "0", required = false) Integer pageNumber, @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize, @RequestParam(required = false, defaultValue = "itemQuantity") String keyword, @RequestParam(required = false, defaultValue = "desc") String order) {
        StoreDto storeDto = storeService.getStore(storeId);
        if (JwtUtils.verifyId(jwt, storeDto.getManager().getManagerId(), false))
            return ResponseEntity.status(HttpStatus.OK).body(itemService.getItemsSortedByStore(storeId, pageNumber, pageSize, keyword, order));
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see items of their own stores!");
    }

    // NEW
    @GetMapping(value = "/get-store-inventory/{id}")
    public ResponseEntity<List<ItemDto>> getStoreInventory(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long storeId) {
        StoreDto storeDto = storeService.getStore(storeId);
        if (JwtUtils.verifyId(jwt, storeDto.getManager().getManagerId(), false))
            return ResponseEntity.status(HttpStatus.OK).body(itemService.getStoreItems(storeId));
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only see their own inventory!");
    }

    @PostMapping(value = "/checkout")
    public ResponseEntity<CartDto> checkout(@Valid @RequestBody CartDto cartDto) {
        Set<ItemInCart> itemInCartSet = new HashSet<>();
        double finalPrice = 0D;
        long quantity = 0;
        for (ItemInCart itemInCart : cartDto.getItems()) {
            ItemDto itemDto = itemService.getItem(itemInCart.getItemIdFk());
            if (itemDto.getItemQuantity() >= itemInCart.getItemQuantity()) {
                quantity += itemInCart.getItemQuantity();
                finalPrice += itemInCart.getPrice() * itemInCart.getItemQuantity();
            } else {
                quantity += itemDto.getItemQuantity();
                finalPrice += itemDto.getPrice() * itemDto.getItemQuantity();
                itemInCart.setItemQuantity(itemDto.getItemQuantity());
            }
            itemInCartSet.add(itemInCart);
        }
        cartDto.setPrice(finalPrice);
        cartDto.setQuantity(quantity);
        cartDto.setItems(itemInCartSet);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @PostMapping(value = "/buy-cart")
    public ResponseEntity<CartDto> buyCart(@Valid @RequestBody CartDto cartDto) {
        List<ItemDto> itemDtoList = new ArrayList<>();
        Set<ItemInCart> itemInCartSet = new HashSet<>();
        List<UserNotificationDto> userNotificationDtoList = new ArrayList<>();
        double finalPrice = 0D;
        long quantity = 0;
        double storePrice;
        for (ItemInCart itemInCart : cartDto.getItems()) {
            ItemDto itemDto = itemService.getItem(itemInCart.getItemIdFk());
            StoreDto storeDto = itemDto.getStore();

            if (itemDto.getItemQuantity() >= itemInCart.getItemQuantity()) {
                quantity += itemInCart.getItemQuantity();
            } else {
                quantity += itemDto.getItemQuantity();
                itemInCart.setItemQuantity(itemDto.getItemQuantity());
            }

            itemInCart.setPrice(itemDto.getPrice());
            storePrice = itemDto.getPrice() * itemInCart.getItemQuantity();
            finalPrice += storePrice;

            itemInCartSet.add(itemInCart);

            itemDto.setItemQuantity(itemDto.getItemQuantity() - itemInCart.getItemQuantity());
            itemDto.getProduct().setQuantity(itemDto.getProduct().getQuantity() - itemInCart.getItemQuantity());

            itemDtoList.add(itemDto);

            storeDto.addRevenue(storePrice);
            storeService.updateStore(storeDto);

            UserNotificationDto userNotificationDto = new UserNotificationDto();
            userNotificationDto.setUserId(storeDto.getManager().getManagerId());
            userNotificationDto.setMessage("A sale of Rs. " + storePrice + " occurred.");
            userNotificationDto.setDescription(itemInCart.getItemQuantity() + " of " + itemDto.getProduct().getProprietaryName() + " was ordered!");
            userNotificationDto.setStatus(NotificationStatus.UNSEEN);
            userNotificationDto.setSeverity(NotificationSeverity.MESSAGE);
            userNotificationDto.setCreatedOn(Date.from(Instant.now()));

            userNotificationDtoList.add(userNotificationDto);

        }

        itemService.updateItem(itemDtoList);
        notificationClient.createUserNotification(userNotificationDtoList);

        cartDto.setPrice(finalPrice);
        cartDto.setQuantity(quantity);
        cartDto.setItems(itemInCartSet);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDto);
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> postExcelRead(String jwt, boolean isUpdate, Long storeId, MultipartFile excelInventoryData) throws InterruptedException, IOException, ExecutionException {
        StoreDto storeDto = storeService.getStore(storeId);
//        if (!JwtUtils.verifyId(jwt, storeDto.getManager().getManagerId(), false))
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can add inventory to their own stores!");
        try {
            ReadInventoryFromExcel readInventoryFromExcel = new ReadInventoryFromExcel(excelInventoryData, notificationClient, modelMapper, modelMapper.map(storeDto, Store.class), categoryService, productService, itemService);
            readInventoryFromExcel.readDataFromExcel(isUpdate);

            CompletableFuture<Boolean> booleanCompletableFuture1 = productService.createProducts(readInventoryFromExcel.getProductList());
            CompletableFuture<Boolean> booleanCompletableFuture3;

            while (true) {
                if (booleanCompletableFuture1.isDone()) {
                    booleanCompletableFuture3 = itemService.createItems(readInventoryFromExcel.getTempInventory());
                    break;
                }
            }

            while (true) {
                if (booleanCompletableFuture3.isDone()) {
                    UserNotificationDto userNotificationDto = new UserNotificationDto();
                    userNotificationDto.setUserId(storeService.getStore(storeId).getManager().getManagerId());
                    userNotificationDto.setMessage("Inventory added successfully.");
                    if (isUpdate)
                        userNotificationDto.setDescription("You have successfully updated " + readInventoryFromExcel.getTempInventory().size() + " items in your inventory.");
                    else
                        userNotificationDto.setDescription("You have successfully added " + readInventoryFromExcel.getTempInventory().size() + " items in your inventory.");
                    userNotificationDto.setStatus(NotificationStatus.UNSEEN);
                    userNotificationDto.setSeverity(NotificationSeverity.MESSAGE);
                    userNotificationDto.setCreatedOn(new Date());

                    return CompletableFuture.completedFuture("Inventory " + (isUpdate ? "updated" : "added") + " Successfully!");
                }
            }

        } catch (IOException e) {
            return CompletableFuture.completedFuture("Inventory " + (isUpdate ? "update" : "add") + " Failed! Please try again!");
        }
    }

    @Async("asyncExecutor")
    @PostMapping(value = "/add-inventory/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<String> addInventory(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long storeId, @RequestPart MultipartFile excelInventoryData) throws InterruptedException, IOException, ExecutionException {
        return postExcelRead(jwt, false, storeId, excelInventoryData);
    }

    @Async("asyncExecutor")
    @PostMapping(value = "/update-inventory/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public CompletableFuture<String> updateInventory(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long storeId, @RequestPart MultipartFile excelInventoryData) throws InterruptedException, IOException, ExecutionException {
        return postExcelRead(jwt, true, storeId, excelInventoryData);
    }

    @PostMapping(value = "/create-store")
    public ResponseEntity<StoreDto> createStore(@RequestHeader("Authorization") String jwt, @RequestBody StoreDto storeDto) {
        Long managerId = storeDto.getManager().getManagerId();
        if (!JwtUtils.verifyId(jwt, managerId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only create stores for themselves!");
        storeDto.setManager(managerService.getManagerById(managerId));
        return ResponseEntity.status(HttpStatus.CREATED).body(storeService.createStore(storeDto));
    }

    @PutMapping(value = "/update-store")
    public ResponseEntity<StoreDto> updateStore(@RequestHeader("Authorization") String jwt, @Valid @RequestBody StoreDto storeDto) {
        Long managerId = storeDto.getManager().getManagerId();
        if (!JwtUtils.verifyId(jwt, managerId, false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only update their own stores!");
        return ResponseEntity.status(HttpStatus.OK).body(storeService.updateStore(storeDto));
    }

    @DeleteMapping(value = "/delete-store/{id}")
    public ResponseEntity<String> deleteStore(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long storeId) {
        if (!JwtUtils.verifyId(jwt, storeService.getStore(storeId).getManager().getManagerId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only delete their own stores!");
        storeService.deleteStore(storeId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "/delete-store-async/{id}")
    @Async("asyncExecutor")
    public CompletableFuture<String> deleteStoreAsync(@RequestHeader("Authorization") String jwt, @PathVariable("id") Long storeId) {
        if (!JwtUtils.verifyId(jwt, storeService.getStore(storeId).getManager().getManagerId(), false))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Managers can only delete their own stores!");
        storeService.deleteStore(storeId);
        return CompletableFuture.completedFuture("Store Deleted");
    }

}
