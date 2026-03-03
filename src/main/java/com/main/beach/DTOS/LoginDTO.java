package com.main.beach.DTOS;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(@NotEmpty String email, @NotEmpty String password) {
}
