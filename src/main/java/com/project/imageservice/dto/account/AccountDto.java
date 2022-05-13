package com.project.imageservice.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private Integer id;
    private String accountName;
    private String email;
    private String createdOn;
    private String updatedOn;

}
