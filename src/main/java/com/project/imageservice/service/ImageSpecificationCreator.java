package com.project.imageservice.service;

import com.project.imageservice.domain.Image;
import com.project.imageservice.domain.Tag;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import java.util.List;

@Service
public class ImageSpecificationCreator {

    public Specification<Image> getImagesByAccountId(Integer accountId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("account"), accountId);
    }

    public Specification<Image> getImagesByOriginalNameIn(String originalName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("originalName"), originalName);
    }

    public Specification<Image> getImagesByContentTypeIn(String contentType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("contentType"), contentType);
    }

    public Specification<Image> getImagesBySizeInBetween(Integer size) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("size"), size);
    }

    public Specification<Image> getImagesByTagsIdsIn(List<Integer> tagsIds) {
        return (root, query, criteriaBuilder) -> {
            Join<Image, Tag> imageTags = root.join("tags");
            return criteriaBuilder.in(imageTags.get("id")).value(tagsIds);
        };
    }
}

