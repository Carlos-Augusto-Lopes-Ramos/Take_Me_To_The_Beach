package com.main.beach.Services;

import com.main.beach.Models.UserModel;
import com.main.beach.Repositories.UserRepository;
import com.main.beach.Util.Criptografinha;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    UserRepository userRepository;

    UserService (UserRepository ur) {
        userRepository = ur;
    }

    public ResponseEntity<String> registerUser(UserModel userDTO) {
        UserModel user = new UserModel();
        user = userDTO;
        user.setUserPassword(Criptografinha.hash(user.getUserPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("OuIééééé");
    }

    public ResponseEntity<String> login(String email, String password, HttpSession session) {

        UserModel user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!Criptografinha.verify(password, user.getUserPassword())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        // 🔥 AQUI A SESSÃO É GERADA E SALVA NO REDIS
        session.setAttribute("USER_ID", user.getIdUser());
        session.setAttribute("USER_EMAIL", user.getEmail());

        return ResponseEntity.ok("Login realizado");
    }
}

