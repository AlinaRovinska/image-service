package com.project.imageservice.service;

import com.project.imageservice.dao.AccountRepository;
import com.project.imageservice.dao.ImageRepository;
import com.project.imageservice.dao.TagRepository;
import com.project.imageservice.domain.Account;
import com.project.imageservice.domain.Image;
import com.project.imageservice.domain.Tag;
import com.project.imageservice.dto.image.CreateImageDto;
import com.project.imageservice.dto.image.ImageDto;
import com.project.imageservice.dto.image.UpdateImageDto;
import com.project.imageservice.excepttion.NoSuchEntityExistException;
import com.project.imageservice.mapper.ImageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;
    private final AccountRepository accountRepository;
    private final TagRepository tagRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ImageDto> getImages(Integer accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchEntityExistException("Did not find the Account id - " + accountId));

        return imageRepository.findAllByAccountId(accountId)
                .stream()
                .map(a -> imageMapper.mapToDo(a))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ImageDto findById(Integer accountId, Integer imageId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchEntityExistException("Did not find the Account id - " + accountId));

        return imageRepository.findByIdAndAccountId(accountId, imageId)
                .map(a -> imageMapper.mapToDo(a))
                .orElseThrow(
                        () -> new NoSuchEntityExistException(
                                String.format(
                                        "Did not find the Image id - %s by Account id - %s",
                                        imageId,
                                        accountId)
                        )
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ImageDto> findBySpecification(
            Specification<Image> specification,
            Pageable pageable
    ) {

        return imageRepository.findAll(specification, pageable)
                .map(i -> imageMapper.mapToDo(i));
    }

    @Override
    @Transactional
    public ImageDto create(
            CreateImageDto createImageDto,
            Integer accountId
    ) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchEntityExistException("Did not find the Account id - " + accountId));

        List<Integer> requestListTags = createImageDto.getTagsIds();
        List<Tag> tags = tagRepository.findByIdIn(createImageDto.getTagsIds());

        validateTags(tags, requestListTags);

        Image image = new Image();

        image.setAccount(account);

        image.setOriginalName(createImageDto.getOriginalName());
        image.setContentType(createImageDto.getContentType());
        image.setSize(createImageDto.getSize());
        LocalDateTime now = LocalDateTime.now();
        image.setCreatedOn(now);
        image.setUpdatedOn(now);
        image.setTags(tags);

        imageRepository.save(image);

        return imageMapper.mapToDo(image);
    }

    @Override
    @Transactional
    public ImageDto update(
            Integer accountId,
            Integer imageId,
            UpdateImageDto updateImageDto
    ) {

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchEntityExistException("Did not find the Account id - " + accountId));

        Image image = imageRepository.findByIdAndAccountId(accountId, imageId)
                .orElseThrow(
                        () -> new NoSuchEntityExistException(String.format(
                                "Did not find the Image id - %s by Account id - %s",
                                imageId,
                                accountId)
                        )
                );


        List<Integer> requestListTags = updateImageDto.getTagsIds();
        List<Tag> tags = tagRepository.findByIdIn(updateImageDto.getTagsIds());

        validateTags(tags, requestListTags);

        image.setAccount(account);

        image.setOriginalName(updateImageDto.getOriginalName());
        image.setContentType(updateImageDto.getContentType());
        image.setSize(updateImageDto.getSize());
        image.setUpdatedOn(LocalDateTime.now());
        image.setTags(tags);

        imageRepository.save(image);


        return imageMapper.mapToDo(image);
    }

    @Override
    @Transactional
    public void delete(
            Integer accountId,
            Integer imageId
    ) {
        imageRepository.deleteByIdAndAccountId(accountId, imageId);
    }

    private void validateTags(List<Tag> tags, List<Integer> requestIds) {

        Set<Integer> dbIds = new HashSet<>();
        for (Tag tag : tags) {
            dbIds.add(tag.getId());
        }

        validateIds(dbIds, requestIds);

    }

    private void validateIds(Set<Integer> dbIds, List<Integer> requestIds) {
        Set<Integer> errorIds = new HashSet<>();
        for (Integer requestTagsId : requestIds) {
            if (!(dbIds.contains(requestTagsId))) {
                errorIds.add(requestTagsId);
            }
        }

        if (!(errorIds.isEmpty())) {
            throw new NoSuchEntityExistException("Did not find the Tag ids - " + errorIds);
        }
    }
}
