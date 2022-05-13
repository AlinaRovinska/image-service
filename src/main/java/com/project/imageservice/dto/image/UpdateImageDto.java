package com.project.imageservice.dto.image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateImageDto {

    @NotBlank
    private String originalName;

    @NotBlank
    private String contentType;

    @NotNull
    @Min(value = 0)
    private Integer size;

    @NotNull
    private List<Integer> tagsIds;

}
