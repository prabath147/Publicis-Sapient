package com.pharmacy.service.utils;

import com.pharmacy.service.model.Product;

import java.util.Date;

public interface ItemMini {
    Long getItemId();
    Product getProduct();
    Long getItemQuantity();
    Double getPrice();
    Date getManufacturedDate();
    Date getExpiryDate();

}
