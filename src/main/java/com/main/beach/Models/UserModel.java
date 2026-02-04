package com.main.beach.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Primary;

import java.util.UUID;

@Setter
@Getter
@Entity
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID idUser;
    private String userName;
    private String userPassword;
    private String email;

}
