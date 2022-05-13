package com.project.imageservice.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.imageservice.dto.image.CreateImageDto;
import com.project.imageservice.dto.image.ImageDto;
import com.project.imageservice.dto.image.UpdateImageDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageControllerTests extends BaseIntegrationTest {


    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatFindAllImagesReturnListOfImages() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/{accountId}/images", 1)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        List<ImageDto> imageDtoList = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertThat(imageDtoList.size()).isEqualTo(1);
        ImageDto imageDto = imageDtoList.get(0);
        assertThat(imageDto.getId()).isEqualTo(1);
        assertThat(imageDto.getOriginalName()).isEqualTo("imageOriginalName");
        assertThat(imageDto.getContentType()).isEqualTo("imageContentType");
        assertThat(imageDto.getSize()).isEqualTo(10);
        assertThat(imageDto.getAccountId()).isEqualTo(1);
    }

    @Test
    public void verifyThatFindAllImagesBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/1/images"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatFindAllImagesByAccountIdNotFoundReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/{accountId}/images", 2)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatFindImageByIdReturnImage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/{accountId}/images/{imageId}", 1, 1)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        ImageDto imageDto = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertThat(imageDto.getId()).isEqualTo(1);
        assertThat(imageDto.getOriginalName()).isEqualTo("imageOriginalName");
        assertThat(imageDto.getContentType()).isEqualTo("imageContentType");
        assertThat(imageDto.getSize()).isEqualTo(10);
        assertThat(imageDto.getAccountId()).isEqualTo(1);
    }

    @Test
    public void verifyThatFindImageByIdBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/1/images/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatFindImageByIdNotInDbReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/{accountId}/images/{imageId}", 1, 2)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatFindImageByAccountIdNotInDbReturn404() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/{accountId}/images/{imageId}", 2, 1)
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
    public void verifyThatCreateImageIsSuccess() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(1);
        tagIds.add(2);

        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setOriginalName("imageOriginalName");
        createImageDto.setContentType("imageContentType");
        createImageDto.setSize(10);
        createImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(createImageDto);

        MvcResult mvcResult = mockMvc.perform(post("/api/accounts/{accountId}/images", 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isOk())
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ImageDto imageDto = objectMapper.readValue(json, ImageDto.class);

        assertThat(imageDto.getId()).isEqualTo(1);
        assertThat(imageDto.getOriginalName()).isEqualTo("imageOriginalName");
        assertThat(imageDto.getContentType()).isEqualTo("imageContentType");
        assertThat(imageDto.getSize()).isEqualTo(10);
        assertThat(imageDto.getAccountId()).isEqualTo(1);
    }

    @Test
    public void verifyThatCreateImageBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/accounts/1/images"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
              """)
    public void verifyThatCreateImageShouldReturnBadRequestWhenOriginalNameIsBlank() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(1);
        tagIds.add(2);

        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setContentType("imageContentType");
        createImageDto.setSize(10);
        createImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(createImageDto);

        mockMvc.perform(post("/api/accounts/{accountId}/images", 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
              """)
    public void verifyThatCreateImageShouldReturnBadRequestWhenContentTypeIsBlank() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(1);
        tagIds.add(2);

        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setOriginalName("imageOriginalName");
        createImageDto.setSize(10);
        createImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(createImageDto);

        mockMvc.perform(post("/api/accounts/{accountId}/images", 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
              """)
    public void verifyThatCreateImageShouldReturnBadRequestWhenSizeIsBlank() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(1);
        tagIds.add(2);

        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setOriginalName("imageOriginalName");
        createImageDto.setContentType("imageContentType");
        createImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(createImageDto);

        mockMvc.perform(post("/api/accounts/{accountId}/images", 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
              """)
    public void verifyThatCreateImageShouldReturnBadRequestWhenTagsIdsIsBlank() throws Exception {
        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setOriginalName("imageOriginalName");
        createImageDto.setContentType("imageContentType");
        createImageDto.setSize(10);

        String content = objectMapper.writeValueAsString(createImageDto);

        mockMvc.perform(post("/api/accounts/{accountId}/images", 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
              """)
    public void verifyThatCreateImageWithAccountIdNotFoundShouldReturn404() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(1);
        tagIds.add(2);

        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setOriginalName("imageOriginalName");
        createImageDto.setContentType("imageContentType");
        createImageDto.setSize(10);
        createImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(createImageDto);

        mockMvc.perform(post("/api/accounts/{accountId}/images", 2)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
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
    public void verifyThatCreateImageWithATagsIdsNotFoundShouldReturn404() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(11);
        tagIds.add(12);

        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setOriginalName("imageOriginalName");
        createImageDto.setContentType("imageContentType");
        createImageDto.setSize(10);
        createImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(createImageDto);

        mockMvc.perform(post("/api/accounts/{accountId}/images", 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatUpdateImageIsSuccess() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(3);
        tagIds.add(4);

        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName("imageOriginalName2");
        updateImageDto.setContentType("imageContentType2");
        updateImageDto.setSize(20);
        updateImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(updateImageDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/accounts/{accountId}/images/{imageId}", 1, 1)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String json = mvcResult.getResponse().getContentAsString();
        ImageDto imageDto = objectMapper.readValue(json, new TypeReference<>() {
        });

        assertThat(imageDto.getId()).isEqualTo(1);
        assertThat(imageDto.getOriginalName()).isEqualTo("imageOriginalName2");
        assertThat(imageDto.getContentType()).isEqualTo("imageContentType2");
        assertThat(imageDto.getSize()).isEqualTo(20);
        assertThat(imageDto.getAccountId()).isEqualTo(1);
    }

    @Test
    public void verifyThatUpdateImageByIdBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/accounts/1/images/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(statements = """
             insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatUpdateImageShouldReturnBadRequestWhenOriginalNameIsBlank() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(3);
        tagIds.add(4);

        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setContentType("imageContentType2");
        updateImageDto.setSize(20);
        updateImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(updateImageDto);

        mockMvc.perform(put("/api/accounts/{accountId}/images/{imageId}", 1, 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatUpdateImageShouldReturnBadRequestWhenContentTypeIsBlank() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(3);
        tagIds.add(4);

        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName("imageOriginalName2");
        updateImageDto.setSize(20);
        updateImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(updateImageDto);

        mockMvc.perform(put("/api/accounts/{accountId}/images/{imageId}", 1, 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatUpdateImageShouldReturnBadRequestWhenSizeIsBlank() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(3);
        tagIds.add(4);

        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName("imageOriginalName2");
        updateImageDto.setContentType("imageContentType2");
        updateImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(updateImageDto);

        mockMvc.perform(put("/api/accounts/{accountId}/images/{imageId}", 1, 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatUpdateImageShouldReturnBadRequestWhenTagsIdsIsBlank() throws Exception {
        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName("imageOriginalName2");
        updateImageDto.setContentType("imageContentType2");
        updateImageDto.setSize(20);

        String content = objectMapper.writeValueAsString(updateImageDto);

        mockMvc.perform(put("/api/accounts/{accountId}/images/{imageId}", 1, 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatUpdateImageWithAccountIdNotFoundShouldReturn404() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(3);
        tagIds.add(4);

        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName("imageOriginalName2");
        updateImageDto.setContentType("imageContentType2");
        updateImageDto.setSize(20);
        updateImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(updateImageDto);

        mockMvc.perform(put("/api/accounts/{accountId}/images/{imageId}", 2, 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatUpdateImageWithAImageIdNotFoundShouldReturn404() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(3);
        tagIds.add(4);

        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName("imageOriginalName2");
        updateImageDto.setContentType("imageContentType2");
        updateImageDto.setSize(20);
        updateImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(updateImageDto);

        mockMvc.perform(put("/api/accounts/{accountId}/images/{imageId}", 1, 2)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatUpdateImageWithATagsIdsNotFoundShouldReturn404() throws Exception {
        List<Integer> tagIds = new ArrayList<>();
        tagIds.add(11);
        tagIds.add(12);

        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName("imageOriginalName2");
        updateImageDto.setContentType("imageContentType2");
        updateImageDto.setSize(20);
        updateImageDto.setTagsIds(tagIds);

        String content = objectMapper.writeValueAsString(updateImageDto);

        mockMvc.perform(put("/api/accounts/{accountId}/images/{imageId}", 1, 1)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Sql(statements = """
            insert into accounts(id, account_name, user_name, email, password, created_on, updated_on) values 
            (1, 'accountName', 'username', 'email', '$2a$10$Xno4ZDR6sVvSULDwcMIEDuLQKAeoqelai2cr4lx9ONT6GN0FF3CVK', '2022-05-11 21:35:49.174691300 +00:00', '2022-05-11 21:35:49.174691300 +00:00');
            insert into accounts_roles(account_id, role_id) values 
            (1, 1);
            insert into images(id, original_name, content_type, size, account_id, created_on, updated_on) values 
            (1, 'imageOriginalName', 'imageContentType', 10, 1, '2022-05-12 21:35:49.174691300 +00:00', '2022-05-12 21:35:49.174691300 +00:00');
            insert into  images_tags(image_id, tag_id) values 
            (1, 1),
            (1, 2);
              """)
    public void verifyThatDeleteImageIsSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/accounts/{accountId}/images/{imageId}", 1, 1)
                        .header("Authorization", "Basic dXNlcm5hbWU6MTIz"))
                .andExpect(status().isOk());
    }

    @Test
    public void verifyThatDeleteImageByIdBeingUnauthorizedShouldReturn401() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/accounts/1/images/1"))
                .andExpect(status().isUnauthorized());
    }

}
