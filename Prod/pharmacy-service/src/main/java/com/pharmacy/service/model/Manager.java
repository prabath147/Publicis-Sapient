package com.pharmacy.service.model;

import lombok.*;
import org.hibernate.Hibernate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Manager {

    @Id
    @Column(name = "manager_id", nullable = false)
    private Long managerId; // This is equal to user-master id of this user.

    @Column(name = "name")
    private String name;

    @Column(name = "phone_no")
    private String phoneNo;

    @Column(name = "license_no")
    private String licenseNo;

    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(name = "last_modified")
    private Date lastModified;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApprovalStatus approvalStatus;

    public Manager(Long managerId) {
        this.managerId = managerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Manager manager = (Manager) o;
        return managerId != null && Objects.equals(managerId, manager.managerId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
