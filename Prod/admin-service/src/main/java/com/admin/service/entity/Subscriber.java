package com.admin.service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@AllArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
public class Subscriber {


    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = UserSubs.class)
    @JsonIgnore
    @ToString.Exclude
    private Set<UserSubs> userSubsSet = new HashSet<>();

    public Subscriber(Long userId) {
        this.userId = userId;
        this.userSubsSet = new HashSet<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subscriber subscriber = (Subscriber) o;
        return userId != null && Objects.equals(userId, subscriber.userId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }


}