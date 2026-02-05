package com.main.beach.Controllers;

import com.main.beach.DTOS.QrStatus;
import com.main.beach.DTOS.RegisterDTO;
import com.main.beach.Services.QrService;
import com.main.beach.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@Validated
public class MainController {

    private final UserService userService;
    private final QrService qrService;

    public MainController(UserService userService, QrService qrService) {
        this.userService = userService;
        this.qrService = qrService;
    }

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(
            @RequestBody @Valid RegisterDTO registerDTO
    ) {
        return userService.registerUser(registerDTO);
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> loginUser(
            @RequestBody @Valid RegisterDTO registerDTO,
            HttpSession session
    ) {
        return userService.login(registerDTO, session);
    }

    /**
     * Endpoint de teste de sessão
     */
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object userId = session.getAttribute("USER_ID");

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(userId);
    }

    /**
     * 1️⃣ Navegador pede o QR Code
     */
    @PostMapping("/auth/qr")
    public ResponseEntity<?> generateQr() {
        String token = qrService.generateQrToken();
        return ResponseEntity.ok(token);
    }



    @GetMapping("/auth/qr/stream/{token}")
    public SseEmitter qrStream(
            @PathVariable String token,
            HttpSession session
    ) {
        return qrService.createEmitter(token, session);
    }

    @PostMapping("/auth/qr/confirm/{token}")
    public ResponseEntity<?> confirmQr(
            @PathVariable String token,
            HttpSession session
    ) {
        Object userId = session.getAttribute("USER_ID");

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        qrService.confirmQr(token, (Long) userId);

        return ResponseEntity.ok().build();
    }

}
