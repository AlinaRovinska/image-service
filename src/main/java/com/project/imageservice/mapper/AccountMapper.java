package com.project.imageservice.mapper;

import com.project.imageservice.domain.Account;
import com.project.imageservice.dto.account.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountDto mapToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setAccountName(account.getAccountName());
        accountDto.setEmail(account.getEmail());
        accountDto.setCreatedOn(account.getCreatedOn().toString());
        accountDto.setUpdatedOn(account.getUpdatedOn().toString());
        return accountDto;
    }
}
