package com.project.imageservice.unit;

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
import com.project.imageservice.service.ImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ImageServiceTests {

    private static final Integer ACCOUNT_ID = 1;
    private static final String ACCOUNT_USERNAME = "someUsername";
    private static final String ACCOUNT_PASSWORD = "123";
    private static final String ACCOUNT_ACCOUNT_NAME = "someAccountName";
    private static final String ACCOUNT_EMAIL = "someEmail";

    private static final Integer IMAGE_ID = 2;
    private static final String IMAGE_ORIGINAL_NAME = "someOriginalName";
    private static final String IMAGE_CONTENT_TYPE = "someContentType";
    private static final Integer IMAGE_SIZE = 10;
    private static final LocalDateTime NOW = LocalDateTime.now();

    private static final String UPDATE_IMAGE_ORIGINAL_NAME = "someOriginalName2";
    private static final String UPDATE_IMAGE_CONTENT_TYPE = "someContentType2";
    private static final Integer UPDATE_IMAGE_SIZE = 11;

    private static final Integer TAG_ID = 3;
    private static final String TAG_NAME = "someTagName";

    private static final Integer UPDATE_TAG_ID = 4;
    private static final String UPDATE_TAG_NAME = "someTagName2";

    private ImageRepository imageRepository;
    private AccountRepository accountRepository;
    private TagRepository tagRepository;

    private ImageServiceImpl imageService;

    @BeforeEach
    public void setUp() {
        imageRepository = mock(ImageRepository.class);
        accountRepository = mock(AccountRepository.class);
        tagRepository = mock(TagRepository.class);
        ImageMapper imageMapper = new ImageMapper();
        imageService = new ImageServiceImpl(imageRepository, imageMapper, accountRepository, tagRepository);

    }

    @Test
    public void whenGetAllImagesByAccountIdNotFoundThenNoSuchEntityExistException() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> imageService.getImages(ACCOUNT_ID));
    }

    @Test
    public void whenGetAllImagesByAccountIdNoDataThenSuccess() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        List<Image> imageList = new ArrayList<>();
        when(imageRepository.findAllByAccountId(ACCOUNT_ID)).thenReturn(imageList);
        List<ImageDto> imageDtoList = imageService.getImages(ACCOUNT_ID);

        assertThat(imageDtoList.size()).isEqualTo(0);
    }

    @Test
    public void whenGetAllImagesByAccountIdShouldReturnListOfAllImages() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        List<Image> imageList = getImageList();
        when(imageRepository.findAllByAccountId(ACCOUNT_ID)).thenReturn(imageList);
        List<ImageDto> imageDtoList = imageService.getImages(ACCOUNT_ID);

        assertThat(imageDtoList.size()).isEqualTo(1);
        ImageDto imageDto = imageDtoList.get(0);
        assertThat(imageDto.getId()).isEqualTo(IMAGE_ID);
        assertThat(imageDto.getOriginalName()).isEqualTo(IMAGE_ORIGINAL_NAME);
        assertThat(imageDto.getContentType()).isEqualTo(IMAGE_CONTENT_TYPE);
        assertThat(imageDto.getSize()).isEqualTo(IMAGE_SIZE);
    }

    @Test
    public void whenGetImageByAccountIdNotFoundThenNoSuchEntityExistException() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> imageService.findById(ACCOUNT_ID, IMAGE_ID));
    }

    @Test
    public void whenGetImageByIdNotFountThenNoSuchEntityExistException() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        when(imageRepository.findById(IMAGE_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> imageService.findById(ACCOUNT_ID, IMAGE_ID));
    }

    @Test
    public void whenGetImageByIdThenSuccess() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        Image image = createImage();
        when(imageRepository.findByIdAndAccountId(ACCOUNT_ID, IMAGE_ID)).thenReturn(Optional.of(image));

        ImageDto imageDto = imageService.findById(ACCOUNT_ID, IMAGE_ID);

        assertThat(imageDto.getId()).isEqualTo(IMAGE_ID);
        assertThat(imageDto.getOriginalName()).isEqualTo(IMAGE_ORIGINAL_NAME);
        assertThat(imageDto.getContentType()).isEqualTo(IMAGE_CONTENT_TYPE);
        assertThat(imageDto.getSize()).isEqualTo(IMAGE_SIZE);
    }

    // find by specification?

    @Test
    public void whenCreateImageAccountIdNotFoundThenNoSuchEntityExistException() {
        CreateImageDto createImageDto = createImageDto();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> imageService.create(createImageDto, ACCOUNT_ID));
    }

    @Test
    public void whenCreateImageTagIdNotFoundThenNoSuchEntityExistException() {
        CreateImageDto createImageDto = createImageDto();
        List<Integer> tagIds = getTagsIds();
        when(tagRepository.findByIdIn(tagIds)).thenReturn(null);
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> imageService.create(createImageDto, ACCOUNT_ID));
    }

    @Test
    public void whenCreateImageThenSuccess() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        List<Integer> tagIds = getTagsIds();
        when(tagRepository.findByIdIn(tagIds)).thenReturn(getTagList());

        CreateImageDto createImageDto = createImageDto();

        ImageDto imageDto = imageService.create(createImageDto, ACCOUNT_ID);

        assertThat(imageDto.getOriginalName()).isEqualTo(IMAGE_ORIGINAL_NAME);
        assertThat(imageDto.getContentType()).isEqualTo(IMAGE_CONTENT_TYPE);
        assertThat(imageDto.getSize()).isEqualTo(IMAGE_SIZE);

        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void whenUpdateImageAccountIdNotFoundThenNoSuchEntityExistException() {
        UpdateImageDto updateImageDto = updateImageDto();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> imageService.update(ACCOUNT_ID, IMAGE_ID, updateImageDto));
    }

    @Test
    public void whenUpdateImageByIdNotFoundThenNoSuchEntityExistException() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        UpdateImageDto updateImageDto = updateImageDto();
        when(imageRepository.findById(IMAGE_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> imageService.update(ACCOUNT_ID, IMAGE_ID, updateImageDto));
    }

    @Test
    public void whenUpdateImageTagIdNotFoundThenNoSuchEntityExistException() {
        UpdateImageDto updateImageDto = updateImageDto();
        List<Integer> tagIds = getTagsIds();
        when(tagRepository.findByIdIn(tagIds)).thenReturn(null);
        assertThatExceptionOfType(NoSuchEntityExistException.class)
                .isThrownBy(() -> imageService.update(ACCOUNT_ID, IMAGE_ID, updateImageDto));
    }

    @Test
    public void whenUpdateImageThenSuccess() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        List<Integer> tagIds = getUpdateTagsIds();
        when(tagRepository.findByIdIn(tagIds)).thenReturn(getUpdateTagList());

        Image image = createImage();
        when(imageRepository.findByIdAndAccountId(ACCOUNT_ID, IMAGE_ID)).thenReturn(Optional.of(image));

        UpdateImageDto updateImageDto = updateImageDto();
        ImageDto imageDto = imageService.update(ACCOUNT_ID, IMAGE_ID, updateImageDto);
        assertThat(imageDto.getOriginalName()).isEqualTo(UPDATE_IMAGE_ORIGINAL_NAME);
        assertThat(imageDto.getContentType()).isEqualTo(UPDATE_IMAGE_CONTENT_TYPE);
        assertThat(imageDto.getSize()).isEqualTo(UPDATE_IMAGE_SIZE);

        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void whenDeleteImageThenSuccess() {
        imageService.delete(ACCOUNT_ID, IMAGE_ID);
        verify(imageRepository, times(1)).deleteByIdAndAccountId(ACCOUNT_ID, IMAGE_ID);
    }


    private Image createImage() {
        Image image = new Image();
        image.setId(IMAGE_ID);
        image.setOriginalName(IMAGE_ORIGINAL_NAME);
        image.setContentType(IMAGE_CONTENT_TYPE);
        image.setSize(IMAGE_SIZE);
        image.setCreatedOn(NOW);
        image.setUpdatedOn(NOW);
        image.setAccount(createAccount());
        image.setTags(getTagList());
        return image;
    }

    private List<Image> getImageList() {
        List<Image> imageList = new ArrayList<>();
        imageList.add(createImage());
        return imageList;
    }

    private Account createAccount() {
        Account account = new Account();
        account.setId(ACCOUNT_ID);
        account.setPassword(ACCOUNT_PASSWORD);
        account.setAccountName(ACCOUNT_ACCOUNT_NAME);
        account.setUserName(ACCOUNT_USERNAME);
        account.setEmail(ACCOUNT_EMAIL);
        account.setCreatedOn(NOW);
        account.setUpdatedOn(NOW);
        return account;
    }

    private Tag createTag() {
        Tag tag = new Tag();
        tag.setId(TAG_ID);
        tag.setTagName(TAG_NAME);
        return tag;
    }

    private List<Tag> getTagList() {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(createTag());
        return tagList;
    }

    private List<Integer> getTagsIds() {
        List<Tag> tagList = getTagList();
        List<Integer> tagsIds = new ArrayList<>();
        for (Tag tag : tagList) {
            tagsIds.add(tag.getId());
        }
        return tagsIds;
    }

    private Tag createUpdateTag() {
        Tag updateTag = new Tag();
        updateTag.setId(UPDATE_TAG_ID);
        updateTag.setTagName(UPDATE_TAG_NAME);
        return updateTag;
    }

    private List<Tag> getUpdateTagList() {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(createUpdateTag());
        return tagList;
    }

    private List<Integer> getUpdateTagsIds() {
        List<Tag> tagList = getUpdateTagList();
        List<Integer> tagsIds = new ArrayList<>();
        for (Tag tag : tagList) {
            tagsIds.add(tag.getId());
        }
        return tagsIds;
    }

    private CreateImageDto createImageDto() {
        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setOriginalName(IMAGE_ORIGINAL_NAME);
        createImageDto.setContentType(IMAGE_CONTENT_TYPE);
        createImageDto.setSize(IMAGE_SIZE);
        createImageDto.setTagsIds(getTagsIds());
        return createImageDto;
    }

    private UpdateImageDto updateImageDto() {
        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName(UPDATE_IMAGE_ORIGINAL_NAME);
        updateImageDto.setContentType(UPDATE_IMAGE_CONTENT_TYPE);
        updateImageDto.setSize(UPDATE_IMAGE_SIZE);
        updateImageDto.setTagsIds(getUpdateTagsIds());
        return updateImageDto;
    }
}
