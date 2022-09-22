package com.pharmacy.service.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StoreDto {
    private Long storeId;

    @NotBlank(message = "store name may not be empty or null")
    private String storeName;
    @Valid
    private AddressDto address;

    private Date createdDate = new Date();

    private ManagerDto manager;

    private Double revenue = 0D;

    public StoreDto(Long storeId, String storeName, AddressDto address, Date createdDate, ManagerDto manager) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.createdDate = createdDate;
        this.manager = manager;
    }

    public StoreDto(Long storeId) {
        this.storeId = storeId;
    }

    public void addRevenue(Double storePrice){
        this.revenue += storePrice;
    }
}
