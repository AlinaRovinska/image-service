package com.project.imageservice.dto.image;

import com.project.imageservice.dto.tag.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {

    private Integer id;
    private String originalName;
    private String contentType;
    private Integer size;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private Integer accountId;
    private List<TagDto> tags;

}
