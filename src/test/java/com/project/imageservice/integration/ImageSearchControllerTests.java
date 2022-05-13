package com.project.imageservice.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.project.imageservice.dto.image.ImageDto;
import com.project.imageservice.integration.dto.PageDto;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ImageSearchControllerTests extends BaseIntegrationTest {

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
    public void verifyThatFindAllImagesByAccountIdShouldReturnPageOfImages() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?accountId=1"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(1);
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
    public void verifyThatFindAllImagesByAccountIdNotInDbShouldReturnEmptyPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?accountId=2"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(0);
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
    public void verifyThatFindAllImagesByOriginalNameShouldReturnPageOfImages() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?originalName=imageOriginalName"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(1);
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
    public void verifyThatFindAllImagesByOriginalNameNotInDbShouldReturnEmptyPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?originalName=imageOriginalName2"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(0);
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
    public void verifyThatFindAllImagesByContentTypeShouldReturnPageOfImages() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?contentType=imageContentType"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(1);
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
    public void verifyThatFindAllImagesByContentTypeNotInDbShouldReturnEmptyPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?contentType=imageContentType2"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(0);
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
    public void verifyThatFindAllImagesBySizeShouldReturnPageOfImages() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?size=10"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(1);
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
    public void verifyThatFindAllImagesBySizeNotInDbShouldReturnEmptyPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?size=20"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(0);
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
    public void verifyThatFindAllImagesByTagsIdsShouldReturnPageOfImages() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?tagsIds=1"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(1);
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
    public void verifyThatFindAllImagesByTagsIdsNotInDbShouldReturnEmptyPage() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/search?tagsIds=20"))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvcResult.getResponse().getContentAsString();
        Page<ImageDto> imageDtoPage = objectMapper.readValue(json, new TypeReference<PageDto<ImageDto>>() {
        });

        assertThat(imageDtoPage.getTotalElements()).isEqualTo(0);
    }
}
