package com.main.beach.DTOS;

import jakarta.validation.constraints.NotBlank;

public record RegisterDTO (@NotBlank String name, @NotBlank String password, @NotBlank String email) {
}
