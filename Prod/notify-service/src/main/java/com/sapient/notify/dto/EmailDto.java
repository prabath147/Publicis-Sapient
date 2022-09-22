package com.sapient.notify.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailDto {
    private String toEmail;
    private Long userId;
    private String emailSubject;
    private String emailBody;
}