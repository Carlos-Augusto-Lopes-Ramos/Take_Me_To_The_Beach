package com.main.beach.DTOS;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public record QrTokenDTO(@NotEmpty String qrToken, @NotNull Instant expiration, String userToken) {
    public boolean checkExpiration(){
        return expiration.isAfter(LocalDateTime.now().toInstant(ZoneOffset.of("-03:00")));
    }

}
