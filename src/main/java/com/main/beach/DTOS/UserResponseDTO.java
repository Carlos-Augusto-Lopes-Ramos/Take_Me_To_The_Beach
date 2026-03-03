package com.main.beach.DTOS;

import jakarta.validation.constraints.NotEmpty;

public record UserResponseDTO (@NotEmpty String name, @NotEmpty String email){

}
