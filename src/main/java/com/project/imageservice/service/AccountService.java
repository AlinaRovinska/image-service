package com.project.imageservice.service;

import com.project.imageservice.dto.account.AccountDto;
import com.project.imageservice.dto.account.CreateAccountDto;
import com.project.imageservice.dto.account.UpdateAccountDto;

import java.util.List;

public interface AccountService {

    List<AccountDto> findAll();

    AccountDto findById(Integer theId);

    AccountDto create(CreateAccountDto dto);

    AccountDto update(Integer accountId, UpdateAccountDto updateAccountDto);

    void deleteById(Integer theId);

}
