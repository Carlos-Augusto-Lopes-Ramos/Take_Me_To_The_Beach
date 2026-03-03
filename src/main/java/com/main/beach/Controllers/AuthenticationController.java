package com.main.beach.Controllers;

import com.main.beach.DTOS.LoginDTO;
import com.main.beach.DTOS.QrTokenDTO;
import com.main.beach.DTOS.RegisterDTO;
import com.main.beach.Services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthenticationController {
    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/proxy")
    public ResponseEntity<String> proxy(){
        return ResponseEntity.ok(LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.UTC).toString());
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(
            @RequestBody @Valid RegisterDTO registerDTO
    ) {
        return userService.registerUser(registerDTO);
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(
            @RequestBody @Valid LoginDTO loginDTO
    ) {
        return userService.login(loginDTO);
    }

    @GetMapping("/generate/token")
    public ResponseEntity<String> generateQrToken() {
        return userService.setQrToken();
    }

    @GetMapping("/token/match/{qrToken}")
    public ResponseEntity<String> matchQrToken(@PathVariable("qrToken") @Valid @NotEmpty String qrToken,@RequestHeader("Authorization") @Valid @NotEmpty String token) {
        return userService.matchQrToken(qrToken, token);
    }


    @GetMapping("/token/{qrToken}")
    public ResponseEntity<String> getQrToken(@PathVariable("qrToken") String qrToken) {
        return userService.getQrToken(qrToken);
    }

    @GetMapping("/all")
    public ResponseEntity<List<QrTokenDTO>> allQrToken() {
        return userService.getAllBitches();
    }
}
