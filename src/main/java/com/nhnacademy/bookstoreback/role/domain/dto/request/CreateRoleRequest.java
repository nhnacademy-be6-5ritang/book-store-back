package com.nhnacademy.bookstoreback.role.domain.dto.request;

import lombok.Builder;

@Builder
public record CreateRoleRequest(String roleName) {
}
