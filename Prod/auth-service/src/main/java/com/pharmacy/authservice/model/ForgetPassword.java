package com.pharmacy.authservice.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "forget_password")
public class ForgetPassword {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id")
//    private Long id;

    @Id
    @Column(name = "code")
    String code;

    @Column(name = "token")
    String token;
}
