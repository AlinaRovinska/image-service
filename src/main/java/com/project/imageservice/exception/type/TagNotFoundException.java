package com.project.imageservice.exception.type;

import java.util.Set;

public class TagNotFoundException extends EntityNotFoundException {

    public TagNotFoundException(Set<Integer> tagIds) {
        super(String.format("Tag with ids '%s' not found", tagIds));
    }
}
