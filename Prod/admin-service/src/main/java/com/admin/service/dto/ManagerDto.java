package com.admin.service.dto;

import com.admin.service.entity.ApprovalStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ManagerDto {
    private Long managerId; // This is equal to user-master id of this user.

    private String name;

    private String phoneNo;

    private String licenseNo;

    private Date registrationDate = new Date();

    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    public ManagerDto(Long managerId) {
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
