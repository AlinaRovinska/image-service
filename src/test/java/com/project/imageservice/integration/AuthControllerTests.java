package com.project.imageservice.integration;

import com.project.imageservice.dto.account.AccountDto;
import com.project.imageservice.dto.account.CreateAccountDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTests extends BaseIntegrationTest {


    @Test
    public void verifyThatRegistrationWorksThroughAllLayers() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setAccountName("someAccountName");
        createAccountDto.setUserName("someUserName");
        createAccountDto.setEmail("someEmail");
        createAccountDto.setPassword("somePassword");

        String content = objectMapper.writeValueAsString(createAccountDto);

        MvcResult mvcResult = mockMvc.perform(post("/registration")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        AccountDto accountDto = objectMapper.readValue(json, AccountDto.class);

        assertThat(accountDto.getAccountName()).isEqualTo("someAccountName");
        assertThat(accountDto.getEmail()).isEqualTo("someEmail");
    }

    @Test
    public void verifyThatRegistrationShouldReturnBadRequestWhenAccountNameIsBlank() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setUserName("someUserName");
        createAccountDto.setEmail("someEmail");
        createAccountDto.setPassword("somePassword");

        String content = objectMapper.writeValueAsString(createAccountDto);

        mockMvc.perform(post("/registration")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyThatRegistrationShouldReturnBadRequestWhenUserNameIsBlank() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setAccountName("someAccountName");
        createAccountDto.setEmail("someEmail");
        createAccountDto.setPassword("somePassword");

        String content = objectMapper.writeValueAsString(createAccountDto);

        mockMvc.perform(post("/registration")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyThatRegistrationShouldReturnBadRequestWhenEmailIsBlank() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setAccountName("someAccountName");
        createAccountDto.setUserName("someUserName");
        createAccountDto.setPassword("somePassword");

        String content = objectMapper.writeValueAsString(createAccountDto);

        mockMvc.perform(post("/registration")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void verifyThatRegistrationShouldReturnBadRequestWhenPasswordIsBlank() throws Exception {
        CreateAccountDto createAccountDto = new CreateAccountDto();
        createAccountDto.setAccountName("someAccountName");
        createAccountDto.setUserName("someUserName");
        createAccountDto.setEmail("someEmail");

        String content = objectMapper.writeValueAsString(createAccountDto);

        mockMvc.perform(post("/registration")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
