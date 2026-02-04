package com.main.beach.Controllers;

import com.main.beach.DTOS.RegisterDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
@Validated
public class MainController {

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterDTO registerDTO) {
        return ResponseEntity.ok("Usuiário criado!");
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {

        Object userId = session.getAttribute("USER_EMAIL");

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(userId);
    }


}
