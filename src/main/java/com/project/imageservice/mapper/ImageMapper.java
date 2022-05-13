package com.project.imageservice.mapper;

import com.project.imageservice.domain.Image;
import com.project.imageservice.dto.image.ImageDto;
import com.project.imageservice.dto.tag.TagDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageMapper {

    public ImageDto mapToDo(Image image) {
        ImageDto imageDto = new ImageDto();
        imageDto.setId(image.getId());
        imageDto.setOriginalName(image.getOriginalName());
        imageDto.setContentType(image.getContentType());
        imageDto.setSize(image.getSize());
        imageDto.setCreatedOn(image.getCreatedOn());
        imageDto.setUpdatedOn(image.getUpdatedOn());
        imageDto.setAccountId(image.getAccount().getId());
        List<TagDto> tags = image.getTags()
                .stream()
                .map(t -> {
                    TagDto tagDto = new TagDto();
                    tagDto.setTagName(t.getTagName());
                    tagDto.setId(t.getId());
                    return tagDto;
                })
                .collect(Collectors.toList());
        imageDto.setTags(tags);
        return imageDto;
    }
}
