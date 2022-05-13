package com.project.imageservice.dao;

import com.project.imageservice.domain.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.*;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Integer>, JpaSpecificationExecutor<Image> {

    @Query("from Image i where i.account.id = :accountId")
    @EntityGraph(attributePaths = {"tags"})
    List<Image> findAllByAccountId(Integer accountId);

    @Query("from Image i where i.account.id = :accountId and i.id = :imageId")
    @EntityGraph(attributePaths = {"tags"})
    Optional<Image> findByIdAndAccountId(Integer accountId, Integer imageId);

    @Query("delete from Image i where i.account.id = :accountId and i.id = :imageId")
    @Modifying
    void deleteByIdAndAccountId(Integer accountId, Integer imageId);

    @EntityGraph(attributePaths = {"tags"})
    Page<Image> findAll(Specification<Image> spec, Pageable pageable);


}


