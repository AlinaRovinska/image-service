package com.project.imageservice.service;

import com.project.imageservice.dao.AccountRepository;
import com.project.imageservice.dao.RoleRepository;
import com.project.imageservice.domain.Account;
import com.project.imageservice.domain.Role;
import com.project.imageservice.dto.account.AccountDto;
import com.project.imageservice.dto.account.CreateAccountDto;
import com.project.imageservice.dto.account.UpdateAccountDto;
import com.project.imageservice.excepttion.AccountWithUsernameExistException;
import com.project.imageservice.excepttion.NoSuchEntityExistException;
import com.project.imageservice.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.project.imageservice.domain.enums.RoleNames.USER;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AccountDto> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(a -> accountMapper.mapToDto(a))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AccountDto findById(Integer theId) {
        return accountRepository.findById(theId)
                .map(a -> accountMapper.mapToDto(a))
                .orElseThrow(() -> new NoSuchEntityExistException("Did not find the Account id - " + theId));
    }

    @Override
    @Transactional
    public AccountDto create(CreateAccountDto createAccountDto) {

        String userName = createAccountDto.getUserName();

        Optional<Account> optionalAccount = accountRepository.findByUserName(userName);

        if (optionalAccount.isPresent()) {
            throw new AccountWithUsernameExistException(
                    String.format("Account with username %s already exists", userName));
        }

        Role role = roleRepository.findByRole(USER)
                .orElseThrow(() -> new NoSuchEntityExistException(String.format("Role not found by name %s",USER)));

        Account account = new Account();
        account.setAccountName(createAccountDto.getAccountName());
        account.setUserName(userName);
        account.setEmail(createAccountDto.getEmail());
        account.setPassword(passwordEncoder.encode(createAccountDto.getPassword()));
        account.setRoles(List.of(role));
        LocalDateTime now = LocalDateTime.now();
        account.setCreatedOn(now);
        account.setUpdatedOn(now);

        accountRepository.save(account);

        return accountMapper.mapToDto(account);
    }

    @Override
    @Transactional
    public AccountDto update(Integer accountId, UpdateAccountDto updateAccountDto) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchEntityExistException("Did not find the Account id - " + accountId));

        account.setAccountName(updateAccountDto.getAccountName());
        account.setUserName(updateAccountDto.getUserName());
        account.setEmail(updateAccountDto.getEmail());
        account.setUpdatedOn(LocalDateTime.now());

        accountRepository.save(account);

        return accountMapper.mapToDto(account);
    }

    @Override
    @Transactional
    public void deleteById(Integer accountId) {
        accountRepository.deleteById(accountId);
    }
}
