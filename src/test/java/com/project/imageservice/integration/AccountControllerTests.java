package com.project.imageservice.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.imageservice.dto.account.AccountDto;
import com.project.imageservice.dto.account.UpdateAccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTests extends BaseIntegrationTest {

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            """)
    public void verifyThatFindAllAccountsReturnListOfAccounts() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts")
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<AccountDto> accountDtoList = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertThat(accountDtoList.size()).isEqualTo(1);
        AccountDto accountDto = accountDtoList.get(0);
        assertThat(accountDto.getId()).isEqualTo(1);
        assertThat(accountDto.getAccountName()).isEqualTo("accountName");
        assertThat(accountDto.getEmail()).isEqualTo("email");
    }

    @Test
    public void verifyThatFindAllAccountsBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            """)
    public void verifyThatFindAccountByIdReturnAccount() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/1")
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        AccountDto accountDto = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertThat(accountDto.getId()).isEqualTo(1);
        assertThat(accountDto.getAccountName()).isEqualTo("accountName");
        assertThat(accountDto.getEmail()).isEqualTo("email");
    }

    @Test
    public void verifyThatFindAllAccountByIdBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values
            (1, 1);
            """)
    public void verifyThatFindAccountByAccountIdNotExistInDbReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/2")
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values
            (1, 1);
            """)
    public void verifyThatUpdateAccountByIdSuccess() throws Exception {
        UpdateAccountDto updateAccountDto = new UpdateAccountDto();
        updateAccountDto.setUserName("username");
        updateAccountDto.setAccountName("accountName2");
        updateAccountDto.setEmail("email2");

        String content = objectMapper.writeValueAsString(updateAccountDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/accounts/{id}", 1)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        AccountDto accountDto = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertThat(accountDto.getAccountName()).isEqualTo("accountName2");
        assertThat(accountDto.getEmail()).isEqualTo("email2");
    }

    @Test
    public void verifyThatUpdateAccountByIdBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/accounts/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values
            (1, 1);
            """)
    public void verifyThatUpdateAccountShouldReturnBadRequestWhenAccountNameIsBlank() throws Exception {
        UpdateAccountDto updateAccountDto = new UpdateAccountDto();
        updateAccountDto.setUserName("username");
        updateAccountDto.setEmail("email2");

        String content = objectMapper.writeValueAsString(updateAccountDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/accounts/{id}", 1)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values
            (1, 1);
            """)
    public void verifyThatUpdateAccountShouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        UpdateAccountDto updateAccountDto = new UpdateAccountDto();
        updateAccountDto.setAccountName("accountName2");
        updateAccountDto.setUserName("username");

        String content = objectMapper.writeValueAsString(updateAccountDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/accounts/{id}", 1)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values
            (1, 1);
            """)
    public void verifyThatUpdateAccountByAccountIdNotExistInDbReturn404() throws Exception {
        UpdateAccountDto updateAccountDto = new UpdateAccountDto();
        updateAccountDto.setUserName("username");
        updateAccountDto.setAccountName("accountName2");
        updateAccountDto.setEmail("email2");

        String content = objectMapper.writeValueAsString(updateAccountDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/accounts/2")
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values
            (1, 1);
            """)
    public void verifyThatDeleteAccountSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/accounts/1")
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isOk());
    }

    @Test
    public void verifyThatDeleteAccountBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/accounts/1"))
                .andExpect(status().isUnauthorized());
    }
}
