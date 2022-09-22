package com.admin.service.entity;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@TypeDefs({@TypeDef(name = "json", typeClass = JsonType.class)})
public class Subscriptions{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id", nullable = false)
    private Long subscriptionId;

    @Column(name = "subscription_name", nullable = false, unique = true)
    private String subscriptionName;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "subscription_cost",nullable = false)
    private Double subscriptionCost;

    @Type(type = "json")
    @Column(columnDefinition = "json")
    private Benefits benefits;

    @Column(name = "period", nullable = false)
    private int period;

    @Column(name = "status")
    private SubscriptionStatus status;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subscriptions subscriptions = (Subscriptions) o;
        return subscriptionId != null && Objects.equals(subscriptionId, subscriptions.subscriptionId);
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

}
