package com.pharmacy.authservice.model;


import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity(name = "refresh_token")
public class RefreshToken {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private long id;

        @OneToOne(cascade = CascadeType.MERGE)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private UserMaster user;

        @Column(nullable = false, unique = true)
        private String token;

        @Column(nullable = false)
        private Instant expiryDate;
}
