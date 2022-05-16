package com.project.imageservice.exception.type;

import com.project.imageservice.domain.enums.RoleNames;

public class RoleNotFoundException extends EntityNotFoundException {

    public RoleNotFoundException(RoleNames role) {
        super(String.format("Role not found by name %s", role.name()));
    }
}
