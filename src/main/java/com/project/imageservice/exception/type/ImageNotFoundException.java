package com.project.imageservice.exception.type;

public class ImageNotFoundException extends EntityNotFoundException {

    public ImageNotFoundException(Integer imageId, Integer accountId) {
        super(String.format("Did not find the Image id - %s by Account id - %s", imageId, accountId));
    }
}
