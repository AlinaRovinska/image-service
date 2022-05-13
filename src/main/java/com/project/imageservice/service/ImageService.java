package com.project.imageservice.service;

import com.project.imageservice.domain.Image;
import com.project.imageservice.dto.image.CreateImageDto;
import com.project.imageservice.dto.image.ImageDto;
import com.project.imageservice.dto.image.UpdateImageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ImageService {

    List<ImageDto> getImages(Integer accountId);

    ImageDto findById(Integer accountId, Integer imageId);

    Page<ImageDto> findBySpecification(Specification<Image> specification, Pageable pageable);

    ImageDto create(CreateImageDto createImageDto, Integer accountId);

    ImageDto update(Integer accountId, Integer imageId, UpdateImageDto updateImageDto);

    void delete(Integer accountId, Integer imageId);
}
