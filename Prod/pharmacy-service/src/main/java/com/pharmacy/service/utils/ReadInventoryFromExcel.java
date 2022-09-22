package com.pharmacy.service.utils;

import com.pharmacy.service.client.notify.NotificationClient;
import com.pharmacy.service.dtoexternal.NotificationSeverity;
import com.pharmacy.service.dtoexternal.NotificationStatus;
import com.pharmacy.service.dtoexternal.UserNotificationDto;
import com.pharmacy.service.exception.ExcelException;
import com.pharmacy.service.model.Category;
import com.pharmacy.service.model.Item;
import com.pharmacy.service.model.Product;
import com.pharmacy.service.model.Store;
import com.pharmacy.service.service.CategoryService;
import com.pharmacy.service.service.ItemService;
import com.pharmacy.service.service.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Getter
@Setter
@Slf4j
@RequiredArgsConstructor
public class ReadInventoryFromExcel {

    private XSSFWorkbook workbook;
    private XSSFSheet worksheet;
    private List<Item> tempInventory;
    private List<Product> productList;
    private List<UserNotificationDto> notifyList;
    private Store store;
    private ItemService itemService;
    private NotificationClient notificationClient;
    private ModelMapper modelMapper;
    private ProductService productService;
    private CategoryService categoryService;
    private MultipartFile excelInventoryData;

    public ReadInventoryFromExcel(MultipartFile excelInventoryData, NotificationClient notificationClient, ModelMapper modelMapper, Store store, CategoryService categoryService, ProductService productService, ItemService itemService) throws IOException {
        try {
            this.notificationClient = notificationClient;
            this.modelMapper = modelMapper;
            this.store = store;
            this.categoryService = categoryService;
            this.productService = productService;
            this.excelInventoryData = excelInventoryData;
            this.workbook = new XSSFWorkbook(excelInventoryData.getInputStream());
            this.worksheet = workbook.getSheetAt(0);
            this.tempInventory = new ArrayList<>();
            this.productList = new ArrayList<>();
            this.notifyList = new ArrayList<>();
            this.itemService = itemService;
        } catch (Exception e) {
            log.info(e.toString());
            throw new ExcelException("Error in Excel File Construction!", e);
        }
    }

    private boolean getProductType(String productType) {
        return productType.strip().equalsIgnoreCase("generic");
    }

    private Set<Category> getCategorySet(String categories) {
        Set<Category> categorySet = new HashSet<>();
        String[] stringList = categories.strip().split(",");
        for (String s : stringList) {
            categorySet.add(categoryService.getCategoryByNameOne(s.strip()));
        }
        log.info(categorySet.toString());
        return categorySet;
    }

    public void readDataFromExcel(boolean isUpdate) throws IOException, ExecutionException, InterruptedException {
        Integer prevItemNo = 0;
        log.info(Date.from(Instant.now()).toString());
        StringBuilder message = new StringBuilder();
        UserNotificationDto userNotificationDto = new UserNotificationDto();
        userNotificationDto.setUserId(this.store.getManager().getManagerId());
        userNotificationDto.setMessage("Error in uploaded data!");
        userNotificationDto.setStatus(NotificationStatus.UNSEEN);
        userNotificationDto.setSeverity(NotificationSeverity.ERROR);
        userNotificationDto.setCreatedOn(new Date());
        userNotificationDto.setDescription(message.toString());
        int isUpdateC = isUpdate ? 1 : 0;
        if (isUpdate) {
            prevItemNo = this.itemService.numberOfItemsInStore(store.getStoreId()).get();
            log.info("no of items in store were " + prevItemNo);
            log.info("store data" + store);
            try {
                log.info("no of rows are " + this.worksheet.getPhysicalNumberOfRows());
            } catch (Exception e) {
                UserNotificationDto userNotificationDto1 = new UserNotificationDto();
                userNotificationDto1.setUserId(this.store.getManager().getManagerId());
                userNotificationDto1.setMessage("Error in uploaded Excel Sheet!");
                userNotificationDto1.setDescription("Could not get number of rows in excel sheet!");
                userNotificationDto1.setStatus(NotificationStatus.UNSEEN);
                userNotificationDto1.setSeverity(NotificationSeverity.ERROR);
                userNotificationDto1.setCreatedOn(Date.from(Instant.now()));
                notifyList.add(userNotificationDto1);
                throw new ExcelException("Could not get number of rows in excel sheet!", e);
            }
        }
        for (int i = 1; i < (isUpdate ? (Math.min(prevItemNo + 1, this.worksheet.getPhysicalNumberOfRows())) : this.worksheet.getPhysicalNumberOfRows()); i++) {
            Item item = new Item();
            XSSFRow row = this.worksheet.getRow(i);
            String proprietaryName = null;
            String productName = null;
            String description = null;
            String dosageForm = null;
            String imageUrl = null;
            Long productId = null;
            Long iQuantity = null;
            Product product = null;
            String col = "";
            boolean productType = true;
            Set<Category> categorySet = new HashSet<>();

            try {
                log.info(isUpdateC + " " + i);
                col = "Proprietary Name";
                proprietaryName = row.getCell(1 + isUpdateC).getStringCellValue();
                col = "Product Name";
                productName = row.getCell(2 + isUpdateC).getStringCellValue();
                col = "Category";
                categorySet = this.getCategorySet(row.getCell(3 + isUpdateC).getStringCellValue());
                log.info(categorySet.toString(), "here");
                col = "Product Type";
                productType = this.getProductType(row.getCell(4 + isUpdateC).getStringCellValue());
                col = "Description";
                description = row.getCell(5 + isUpdateC).getStringCellValue();
                col = "Dosage Form";
                dosageForm = row.getCell(6 + isUpdateC).getStringCellValue();
                col = "Image URL";
                imageUrl = row.getCell(7 + isUpdateC).getStringCellValue();

                if (isUpdate) {
                    col = "Item ID";
                    item.setItemId((long) row.getCell(0).getNumericCellValue());
                    iQuantity = itemService.getItem(item.getItemId()).getItemQuantity();
                }
                col = "Manufactured Date";
                item.setManufacturedDate(row.getCell(8 + isUpdateC).getDateCellValue());
                col = "Expiry Date";
                item.setExpiryDate(row.getCell(9 + isUpdateC).getDateCellValue());
                col = "Item Quantity";
                item.setItemQuantity((long) row.getCell(10 + isUpdateC).getNumericCellValue());
                col = "Price";
                item.setPrice(row.getCell(11 + isUpdateC).getNumericCellValue());
                item.setStore(store);

                col = "Product ID";
                productId = (long) row.getCell(isUpdateC).getNumericCellValue();

                if (productService.checkIfExists(productId)) {
                    Long productQuantity = productService.getProduct(productId).getQuantity();
                    product = new Product(productId, proprietaryName, productName, description, dosageForm, imageUrl, productType, categorySet, isUpdate ? productQuantity + item.getItemQuantity() - iQuantity : productQuantity + item.getItemQuantity());
                } else {
                    product = new Product(productId, proprietaryName, productName, description, dosageForm, imageUrl, productType, categorySet, item.getItemQuantity());
                }

                log.info(product.toString());
                item.setProduct(product);
                this.productList.add(product);
                this.tempInventory.add(item);

            } catch (Exception e) {
                log.info("Error in add inventory function is " + e.getMessage());
                message.append("There is an error on row ").append(i).append(" column ").append(col).append(".\nError is ").append(e.getMessage());
                log.info(message.toString());
            }
        }
        log.info("out here");
        workbook.close();
        String msg = message.toString();
        if (!msg.isEmpty()) {
            userNotificationDto.setDescription(msg);
            notifyList.add(userNotificationDto);
        }
        try {
            if (!notifyList.isEmpty()) {
                log.info(notifyList.toString());
                notificationClient.createUserNotification(this.notifyList);
            }
        } catch (Exception e) {
            log.info("Notification Failed: " + e);
            log.info("Notifications are: " + notifyList.toString());
        }
    }


}
