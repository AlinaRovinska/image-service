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
import com.project.imageservice.exception.type.AccountNotFoundException;
import com.project.imageservice.exception.type.ImageNotFoundException;
import com.project.imageservice.exception.type.TagNotFoundException;
import com.project.imageservice.mapper.ImageMapper;
import com.project.imageservice.service.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @Mock
    private ImageRepository imageRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TagRepository tagRepository;
    @Spy
    private ImageMapper imageMapper;
    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    public void whenGetAllImagesByAccountIdNotFoundThenAccountNotFoundException() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(AccountNotFoundException.class)
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

        List<Image> imageList = getImagesByAccount(account);
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
    public void whenGetImageByAccountIdNotFoundThenAccountNotFoundException() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(() -> imageService.findById(ACCOUNT_ID, IMAGE_ID));
    }

    @Test
    public void whenGetImageByIdNotFountThenImageNotFoundException() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        when(imageRepository.findByIdAndAccountId(ACCOUNT_ID, IMAGE_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(ImageNotFoundException.class)
                .isThrownBy(() -> imageService.findById(ACCOUNT_ID, IMAGE_ID));
    }

    @Test
    public void whenGetImageByIdThenSuccess() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        Image image = createImage(account, List.of());
        when(imageRepository.findByIdAndAccountId(ACCOUNT_ID, IMAGE_ID)).thenReturn(Optional.of(image));

        ImageDto imageDto = imageService.findById(ACCOUNT_ID, IMAGE_ID);

        assertThat(imageDto.getId()).isEqualTo(IMAGE_ID);
        assertThat(imageDto.getOriginalName()).isEqualTo(IMAGE_ORIGINAL_NAME);
        assertThat(imageDto.getContentType()).isEqualTo(IMAGE_CONTENT_TYPE);
        assertThat(imageDto.getSize()).isEqualTo(IMAGE_SIZE);
        assertThat(imageDto.getTags()).isNotNull();
        assertThat(imageDto.getTags().size()).isEqualTo(0);
    }

    @Test
    public void whenCreateImageAccountIdNotFoundThenAccountNotFoundException() {
        CreateImageDto createImageDto = createImageDto(List.of());
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(() -> imageService.create(createImageDto, ACCOUNT_ID));
    }

    @Test
    public void whenCreateImageTagIdNotFoundThenTagNotFoundException() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        CreateImageDto createImageDto = createImageDto(List.of(TAG_ID));
        List<Integer> tagIds = createImageDto.getTagsIds();

        when(tagRepository.findByIdIn(tagIds)).thenReturn(List.of());
        assertThatExceptionOfType(TagNotFoundException.class)
                .isThrownBy(() -> imageService.create(createImageDto, ACCOUNT_ID));
    }

    @Test
    public void whenCreateImageThenSuccess() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        List<Tag> tags = createTags();
        List<Integer> tagIds = tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());
        when(tagRepository.findByIdIn(tagIds)).thenReturn(tags);

        CreateImageDto createImageDto = createImageDto(tagIds);

        ImageDto imageDto = imageService.create(createImageDto, ACCOUNT_ID);

        assertThat(imageDto.getOriginalName()).isEqualTo(IMAGE_ORIGINAL_NAME);
        assertThat(imageDto.getContentType()).isEqualTo(IMAGE_CONTENT_TYPE);
        assertThat(imageDto.getSize()).isEqualTo(IMAGE_SIZE);

        verify(imageRepository, times(1)).save(any(Image.class));
    }

    @Test
    public void whenUpdateImageAccountIdNotFoundThenAccountNotFoundException() {
        UpdateImageDto updateImageDto = updateImageDto(List.of(TAG_ID));
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(AccountNotFoundException.class)
                .isThrownBy(() -> imageService.update(ACCOUNT_ID, IMAGE_ID, updateImageDto));
    }

    @Test
    public void whenUpdateImageByIdNotFoundThenImageNotFoundException() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        UpdateImageDto updateImageDto = updateImageDto(List.of(TAG_ID));
        when(imageRepository.findByIdAndAccountId(ACCOUNT_ID, IMAGE_ID)).thenReturn(Optional.empty());
        assertThatExceptionOfType(ImageNotFoundException.class)
                .isThrownBy(() -> imageService.update(ACCOUNT_ID, IMAGE_ID, updateImageDto));
    }

    @Test
    public void whenUpdateImageWithTagIdNotFoundThenTagNotFoundException() {
        Account account = createAccount();
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));

        Image image = createImage(account, List.of());
        when(imageRepository.findByIdAndAccountId(ACCOUNT_ID, IMAGE_ID)).thenReturn(Optional.of(image));

        UpdateImageDto updateImageDto = updateImageDto(List.of(UPDATE_TAG_ID));
        List<Integer> tagIds = updateImageDto.getTagsIds();
        when(tagRepository.findByIdIn(tagIds)).thenReturn(List.of());
        assertThatExceptionOfType(TagNotFoundException.class)
                .isThrownBy(() -> imageService.update(ACCOUNT_ID, IMAGE_ID, updateImageDto));
    }

    @Test
    public void whenUpdateImageThenSuccess() {
        Account account = createAccount();
        List<Tag> tags = createTagsForUpdate();
        List<Integer> tagsIds = tags.stream()
                .map(Tag::getId)
                .collect(Collectors.toList());
        UpdateImageDto updateImageDto = updateImageDto(tagsIds);
        Image image = createImage(account, tags);

        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(account));
        when(imageRepository.findByIdAndAccountId(ACCOUNT_ID, IMAGE_ID)).thenReturn(Optional.of(image));
        when(tagRepository.findByIdIn(tagsIds)).thenReturn(tags);

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


    private Image createImage(Account account, List<Tag> tags) {
        Image image = new Image();
        image.setId(IMAGE_ID);
        image.setOriginalName(IMAGE_ORIGINAL_NAME);
        image.setContentType(IMAGE_CONTENT_TYPE);
        image.setSize(IMAGE_SIZE);
        image.setCreatedOn(NOW);
        image.setUpdatedOn(NOW);
        image.setAccount(account);
        image.setTags(tags);
        return image;
    }

    private List<Image> getImagesByAccount(Account account) {
        List<Tag> tags = createTags();
        List<Image> imageList = new ArrayList<>();
        imageList.add(createImage(account, tags));
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

    private List<Tag> createTags() {
        return List.of(createTag());
    }

    private Tag createTagForUpdate() {
        Tag updateTag = new Tag();
        updateTag.setId(UPDATE_TAG_ID);
        updateTag.setTagName(UPDATE_TAG_NAME);
        return updateTag;
    }

    private List<Tag> createTagsForUpdate() {
        return List.of(createTagForUpdate());
    }

    private CreateImageDto createImageDto(List<Integer> tagIds) {
        CreateImageDto createImageDto = new CreateImageDto();
        createImageDto.setOriginalName(IMAGE_ORIGINAL_NAME);
        createImageDto.setContentType(IMAGE_CONTENT_TYPE);
        createImageDto.setSize(IMAGE_SIZE);
        createImageDto.setTagsIds(tagIds);
        return createImageDto;
    }

    private UpdateImageDto updateImageDto(List<Integer> tagIds) {
        UpdateImageDto updateImageDto = new UpdateImageDto();
        updateImageDto.setOriginalName(UPDATE_IMAGE_ORIGINAL_NAME);
        updateImageDto.setContentType(UPDATE_IMAGE_CONTENT_TYPE);
        updateImageDto.setSize(UPDATE_IMAGE_SIZE);
        updateImageDto.setTagsIds(tagIds);
        return updateImageDto;
    }
}
