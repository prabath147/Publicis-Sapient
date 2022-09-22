package com.sapient.notify.dto;

import com.sapient.notify.model.NotificationSeverity;
import com.sapient.notify.model.NotificationStatus;
import com.sapient.notify.model.UserNotification;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotificationDto {
    private Long id;
    @NotBlank(message = "Message field cannot be empty")
    private String message;
    private String description;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    @Enumerated(EnumType.STRING)
    private NotificationSeverity severity;
    private Date createdOn;
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserNotification notification = (UserNotification) o;
        return id != null && Objects.equals(id, notification.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
