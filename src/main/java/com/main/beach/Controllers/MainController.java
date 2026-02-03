package com.main.beach.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class MainController {

    @PostMapping("/user/register")
    public ResponseEntity<String> registerUser() {
        return ResponseEntity.ok("Usuiário criado!");
    }

}
