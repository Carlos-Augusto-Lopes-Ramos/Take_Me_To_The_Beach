package com.main.beach.DTOS;

import com.main.beach.Models.UserModel;
import jakarta.validation.constraints.NotBlank;

public record RegisterDTO (@NotBlank String name, @NotBlank String password, @NotBlank String email) {
    public UserModel getAtributes(){
        UserModel user = new UserModel();
        user.setUserName(name);
        user.setUserPassword(password);
        user.setEmail(email);
        return user;
    }
}
