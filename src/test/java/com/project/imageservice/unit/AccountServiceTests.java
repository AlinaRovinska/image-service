package com.project.imageservice.unit;

import com.project.imageservice.dao.AccountRepository;
import com.project.imageservice.dao.RoleRepository;
import com.project.imageservice.domain.Account;
import com.project.imageservice.domain.Role;
import com.project.imageservice.domain.enums.RoleNames;
import com.project.imageservice.dto.account.AccountDto;
import com.project.imageservice.dto.account.CreateAccountDto;
import com.project.imageservice.dto.account.UpdateAccountDto;
import com.project.imageservice.excepttion.AccountWithUsernameExistException;
import com.project.imageservice.excepttion.NoSuchEntityExistException;
import com.project.imageservice.mapper.AccountMapper;
import com.project.imageservice.service.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

public class AccountServiceTests {

    private static final Integer ACCOUNT_ID = 1;
    private static final String ACCOUNT_USERNAME = "someUsername";
    private static final String ACCOUNT_PASSWORD = "123";
    private static final String ACCOUNT_ACCOUNT_NAME = "someAccountName";
    private static final String ACCOUNT_EMAIL = "someEmail";
    private static final LocalDateTime NOW = LocalDateTime.now();

    private static final Integer ROLE_ID = 2;

    private static final String UPDATE_ACCOUNT_USERNAME = "someUserName2";
    private static final String UPDATE_ACCOUNT_ACCOUNT_NAME = "someAccountName2";
    private static final String UPDATE_ACCOUNT_EMAIL = "someEmail2";

    private AccountRepository accountRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder encoder;

    private AccountServiceImpl accountService;

    @BeforeEach
    public void setup() {
        accountRepository = mock(AccountRepository.class);
        roleRepository = mock(RoleRepository.class);
        encoder = mock(BCryptPasswordEncoder.class);
        AccountMapper accountMapper = new AccountMapper();
        accountService = new AccountServiceImpl(accountRepository, accountMapper, encoder, roleRepository);
    }

    @Test
    public void whenGetAllAccountsShouldReturnListOfAllAccounts() {
        List<Account> accountList = getAccountList();
        when(accountRepository.findAll()).thenReturn(accountList);
        List<AccountDto> accountDtoList = accountService.findAll();

        assertThat(accountDtoList.size()).isEqualTo(1);
        AccountDto accountDto = accountDtoList.get(0);
        assertThat(accountDto.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(accountDto.getAccountName()).isEqualTo(ACCOUNT_ACCOUNT_NAME);
        assertThat(accountDto.getEmail()).isEqualTo(ACCOUNT_EMAIL);
    }

    @Test
    public void whenNoDataInDbThenSuccess() {
        List<Account> accountList = new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(accountList);
        List<AccountDto> accountDtoList = accountService.findAll();

        assertThat(accountDtoList.size()).isEqualTo(0);
    }

    @Test
    public void whenAccountByIdNotFoundThenNoSuchEntityExistException() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> accountService.findById(ACCOUNT_ID));
    }

    @Test
    public void whenAccountByIdThenSuccess() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        AccountDto accountDto = accountService.findById(ACCOUNT_ID);

        assertThat(accountDto.getId()).isEqualTo(ACCOUNT_ID);
        assertThat(accountDto.getAccountName()).isEqualTo(ACCOUNT_ACCOUNT_NAME);
        assertThat(accountDto.getEmail()).isEqualTo(ACCOUNT_EMAIL);
    }

    @Test
    public void whenCreateAccountWithUserNameThatExistThenAccountWithUserNameExistException() {
        when(accountRepository.findByUserName(ACCOUNT_USERNAME)).thenReturn(Optional.of(new Account()));
        CreateAccountDto createAccountDto = createAccountDto();

        assertThatExceptionOfType(AccountWithUsernameExistException.class)
                .isThrownBy(() -> accountService.create(createAccountDto));
    }

    @Test
    public void whenCreateAccountWithRoleNoFoundThenNoSuchEntityExistException() {
        when(accountRepository.findByUserName(ACCOUNT_USERNAME)).thenReturn(Optional.empty());
        when(roleRepository.findByRole(RoleNames.USER)).thenReturn(Optional.empty());

        CreateAccountDto createAccountDto = createAccountDto();

        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> accountService.create(createAccountDto));


    }

    @Test
    public void whenCreateAccountThenSuccess() {
        when(accountRepository.findByUserName(ACCOUNT_USERNAME)).thenReturn(Optional.empty());

        Role role = createRole();
        when(roleRepository.findByRole(RoleNames.USER)).thenReturn(Optional.of(role));

        when(encoder.encode(ACCOUNT_PASSWORD)).thenReturn(ACCOUNT_PASSWORD);

        CreateAccountDto createAccountDto = createAccountDto();

        AccountDto accountDto = accountService.create(createAccountDto);
        assertThat(accountDto.getAccountName()).isEqualTo(ACCOUNT_ACCOUNT_NAME);
        assertThat(accountDto.getEmail()).isEqualTo(ACCOUNT_EMAIL);

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void whenUpdateAccountWithAccountIdNotExistThenNoSuchEntityException() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> accountService.findById(ACCOUNT_ID));
    }

    @Test
    public void whenUpdateAccountThenSuccess() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        UpdateAccountDto updateAccountDto = updateAccountDto();

        AccountDto accountDto = accountService.update(ACCOUNT_ID, updateAccountDto);
        assertThat(accountDto.getAccountName()).isEqualTo(UPDATE_ACCOUNT_ACCOUNT_NAME);
        assertThat(accountDto.getEmail()).isEqualTo(UPDATE_ACCOUNT_EMAIL);

        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    public void whenDeleteAccountThenSuccess() {
        accountService.deleteById(ACCOUNT_ID);
        verify(accountRepository, times(1)).deleteById(ACCOUNT_ID);
    }


    private Account createAccount() {
        Account account = new Account();
        account.setId(ACCOUNT_ID);
        account.setPassword(ACCOUNT_PASSWORD);
        account.setAccountName(ACCOUNT_ACCOUNT_NAME);
        account.setUserName(ACCOUNT_USERNAME);
        account.setEmail(ACCOUNT_EMAIL);
        account.setCreatedOn(NOW);
        account.setUpdatedOn(NOW);
        return account;
    }

    private List<Account> getAccountList() {
        List<Account> accountList = new ArrayList<>();
        accountList.add(createAccount());
        return accountList;
    }

    private CreateAccountDto createAccountDto() {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setAccountName(ACCOUNT_ACCOUNT_NAME);
        createAccountDto.setUserName(ACCOUNT_USERNAME);
        createAccountDto.setPassword(ACCOUNT_PASSWORD);
        createAccountDto.setEmail(ACCOUNT_EMAIL);
        return createAccountDto;
    }

    private Role createRole() {
        Role role = new Role();
        role.setId(ROLE_ID);
        role.setRole(RoleNames.USER);
        return role;
    }

    private UpdateAccountDto updateAccountDto() {
        UpdateAccountDto updateAccountDto = new UpdateAccountDto();
        updateAccountDto.setUserName(UPDATE_ACCOUNT_USERNAME);
        updateAccountDto.setAccountName(UPDATE_ACCOUNT_ACCOUNT_NAME);
        updateAccountDto.setEmail(UPDATE_ACCOUNT_EMAIL);
        return updateAccountDto;
    }


}
