package com.main.beach.Controllers;

import com.main.beach.DTOS.UserResponseDTO;
import com.main.beach.Models.UserModel;
import com.main.beach.Services.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/public")
@Validated
public class MainController {

    private final UserService userService;

    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/me")
    public ResponseEntity<UserResponseDTO> getMe(@RequestHeader("Authorization") String token){
        return userService.getMe(token);
    }

}
