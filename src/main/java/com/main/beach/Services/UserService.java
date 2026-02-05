package com.main.beach.Services;

import com.main.beach.DTOS.RegisterDTO;
import com.main.beach.Models.UserModel;
import com.main.beach.Repositories.UserRepository;
import com.main.beach.Util.Criptografinha;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> registerUser(RegisterDTO userDTO) {
        UserModel user = new UserModel();
        user = userDTO.getAtributes();
        user.setUserPassword(Criptografinha.hash(user.getUserPassword()));
        if(userRepository.findByEmail(userDTO.email()).isEmpty()) {
            userRepository.save(user);
            return ResponseEntity.ok("Usuário cadastrado!");
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Email já cadastado!");
    }

    public ResponseEntity<String> login(RegisterDTO userDTO, HttpSession session) {

        UserModel user = userRepository.findByEmail(userDTO.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!Criptografinha.verify(userDTO.password(), user.getUserPassword())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }

        // 🔥 AQUI A SESSÃO É GERADA E SALVA NO REDIS
        session.setAttribute("USER_ID", user.getIdUser());
        session.setAttribute("USER_EMAIL", user.getEmail());
        return ResponseEntity.ok(session.getId());
    }
}

