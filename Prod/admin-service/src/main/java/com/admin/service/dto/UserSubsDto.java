package com.admin.service.dto;

import com.admin.service.entity.PaidStatus;
import com.admin.service.entity.Subscriptions;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSubsDto {

    private Long userSubId;
    private Long userId;
    private SubscriptionsDto subscriptions;
    private Date lastPaidDate = new Date();
    private Date subEndDate;
    private PaidStatus status = PaidStatus.PENDING;
}
