package com.pharmacy.service.dto;

import com.pharmacy.service.model.ApprovalStatus;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ManagerDto {
    private Long managerId; // This is equal to user-master id of this user.

    private String name;
    @Size(max = 12, message = "phone number cannot be more than 12 digits")
    private String phoneNo;

    @Size(max = 12, message = "license number cannot be more than 12 digits")
    private String licenseNo;

    private Date registrationDate = new Date();

    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    public ManagerDto(Long managerId){
        this.managerId = managerId;
    }

    public ManagerDto(String name, String phoneNo, String licenseNo, Date registrationDate, ApprovalStatus approvalStatus) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.licenseNo = licenseNo;
        this.registrationDate = registrationDate;
        this.approvalStatus = approvalStatus;
    }
}
