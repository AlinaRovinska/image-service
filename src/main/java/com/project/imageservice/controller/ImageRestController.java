package com.project.imageservice.controller;

import com.project.imageservice.dto.image.CreateImageDto;
import com.project.imageservice.dto.image.ImageDto;
import com.project.imageservice.dto.image.UpdateImageDto;
import com.project.imageservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts/{accountId}/images")
public class ImageRestController {

    private final ImageService imageService;

    @GetMapping
    public List<ImageDto> getImages(
            @PathVariable Integer accountId
    ) {
        return imageService.getImages(accountId);
    }

    @GetMapping("/{imageId}")
    public ImageDto getImage(
            @PathVariable Integer accountId,
            @PathVariable Integer imageId
    ) {
        return imageService.findById(accountId, imageId);
    }

    @PostMapping
    public ImageDto createImage(
            @RequestBody @Valid CreateImageDto createImageDto,
            @PathVariable Integer accountId
    ) {
        return imageService.create(createImageDto, accountId);
    }

    @PutMapping("/{imageId}")
    public ImageDto updateImage(
            @PathVariable Integer accountId,
            @PathVariable Integer imageId,
            @RequestBody @Valid UpdateImageDto updateImageDto
    ) {

        return imageService.update(accountId, imageId, updateImageDto);
    }

    @DeleteMapping("/{imageId}")
    public void deleteImage(
            @PathVariable Integer accountId,
            @PathVariable Integer imageId
    ) {
        imageService.delete(accountId, imageId);
    }

}
