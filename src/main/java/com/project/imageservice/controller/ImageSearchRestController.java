package com.project.imageservice.controller;

import com.project.imageservice.domain.Image;
import com.project.imageservice.dto.image.ImageDto;
import com.project.imageservice.service.ImageService;
import com.project.imageservice.service.ImageSpecificationCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageSearchRestController {

    private final ImageService imageService;
    private final ImageSpecificationCreator imageSpecificationCreator;

    @GetMapping("/search")
    public Page<ImageDto> getImages(
            @RequestParam(required = false) Integer accountId,
            @RequestParam(required = false) String originalName,
            @RequestParam(required = false) String contentType,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) List<Integer> tagsIds,
            Pageable pageable
    ) {
        Specification<Image> spec = Specification.where(null);

        if (accountId != null) {
            spec = spec.and(imageSpecificationCreator.getImagesByAccountId(accountId));
        }
        if (originalName != null) {
            spec = spec.and(imageSpecificationCreator.getImagesByOriginalNameIn(originalName));
        }
        if (contentType != null) {
            spec = spec.and(imageSpecificationCreator.getImagesByContentTypeIn(contentType));
        }
        if (size != null) {
            spec = spec.and(imageSpecificationCreator.getImagesBySizeInBetween(size));
        }
        if (tagsIds != null) {
            spec = spec.and(imageSpecificationCreator.getImagesByTagsIdsIn(tagsIds));
        }

        return imageService.findBySpecification(spec, pageable);
    }
}
