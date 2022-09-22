package com.admin.service.dto;

import com.admin.service.entity.Benefits;
import com.admin.service.entity.SubscriptionStatus;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionsDto{

    private Long subscriptionId;
    @NotEmpty
    @Size(min=4,message = "Subscription title should have at least 4 characters")
    private String subscriptionName;
    @NotEmpty
    @Size(min=10,message = "Subscription Description should have at least 10 characters")
    private String description;

    private Date startDate=new Date();
    @NotNull(message="Subscription End Date should not be null or empty")
    private Date endDate;
    @NotNull(message="Subscription Cost should not be null or empty")
    private Double subscriptionCost;
    @NotNull(message="Subscription Benefits Object should not be null or empty")
    private Benefits benefits;
    @NotNull(message="Subscription Period should not be null or empty")
    private int period;

    private SubscriptionStatus status;
}
