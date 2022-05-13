package com.project.imageservice.controller;

import com.project.imageservice.dto.account.AccountDto;
import com.project.imageservice.dto.account.UpdateAccountDto;
import com.project.imageservice.security.MyPrincipal;
import com.project.imageservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountRestController {

    private final AccountService accountService;

    @GetMapping
    public List<AccountDto> findAll(
            @AuthenticationPrincipal MyPrincipal myPrincipal
    ) {
        return accountService.findAll();
    }

    @GetMapping("/{accountId}")
    public AccountDto getAccount(
            @PathVariable Integer accountId
    ) {
        return accountService.findById(accountId);
    }


    @PutMapping("/{accountId}")
    public AccountDto updateAccount(
            @PathVariable Integer accountId,
            @RequestBody @Valid UpdateAccountDto updateAccountDto
    ) {
        return accountService.update(accountId, updateAccountDto);
    }

    @DeleteMapping("/{accountId}")
    public void deleteAccount(
            @PathVariable Integer accountId
    ) {
        accountService.deleteById(accountId);
    }
}
