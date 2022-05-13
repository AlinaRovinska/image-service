package com.project.imageservice.controller;

import com.project.imageservice.dto.account.AccountDto;
import com.project.imageservice.dto.account.CreateAccountDto;
import com.project.imageservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthRestController {

    private final AccountService accountService;

    @PostMapping("/registration")
    public AccountDto createAccount(
             @RequestBody @Valid CreateAccountDto createAccountDto
    ) {
        return accountService.create(createAccountDto);
    }
}
