package com.admin.service.dto;

import com.admin.service.entity.PaidStatus;
import com.admin.service.entity.UserSubs;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriberDto {
    @NotNull(message="User Id can't be null or empty")
    private Long userId;
    private Set<UserSubsDto> userSubsSet = new HashSet<>();
}
