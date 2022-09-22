package com.admin.service.entity;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserSubs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_sub_id", nullable = false)
    private Long userSubId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "subscription", referencedColumnName = "subscription_id")
    private Subscriptions subscriptions;


    @Column(name = "last_paid_date")
    @Temporal(TemporalType.DATE)
    private Date lastPaidDate = new Date();

    @Column(name = "sub_end_date")
    @Temporal(TemporalType.DATE)
    private Date subEndDate;

    @Column(name = "status")
    private PaidStatus status = PaidStatus.PENDING;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserSubs userSubs = (UserSubs) o;
        return userSubId != null && Objects.equals(userSubId, userSubs.userSubId);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
