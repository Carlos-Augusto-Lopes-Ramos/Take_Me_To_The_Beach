package com.main.beach.Services;

import com.auth0.jwt.JWT;
import com.main.beach.DTOS.LoginDTO;
import com.main.beach.DTOS.QrTokenDTO;
import com.main.beach.DTOS.RegisterDTO;
import com.main.beach.DTOS.UserResponseDTO;
import com.main.beach.Models.UserModel;
import com.main.beach.Models.UserRole;
import com.main.beach.Repositories.UserRepository;
import com.main.beach.Util.Criptografinha;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.yaml.snakeyaml.emitter.Emitter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    private final List<QrTokenDTO> qrTokens;

    public UserService(UserRepository userRepository, TokenService tokenService, List<QrTokenDTO> qrTokens) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.qrTokens = qrTokens;
    }

    public ResponseEntity<String> registerUser(RegisterDTO userDTO) {
        UserModel user = new UserModel();
        user = userDTO.getAtributes();
        user.setUserPassword(Criptografinha.hash(user.getUserPassword()));
        user.setRole(UserRole.USER);
        if(userRepository.findByEmail(userDTO.email()).isEmpty()) {
            userRepository.save(user);
            return ResponseEntity.ok(tokenService.generateToken(user));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Email já cadastado!");
    }

    public ResponseEntity<String> login(LoginDTO userDTO) {
        Optional<UserModel> user = userRepository.findByEmail(userDTO.email());
        if(user.isEmpty()) {
            return ResponseEntity.status(404).body("Usuário não cadastrado!");
        }
        if (!Criptografinha.verify(userDTO.password(), user.get().getUserPassword())) {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
        String token = tokenService.generateToken(user.get());

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<UserResponseDTO> getMe(String token){
        UserModel user = userRepository.findByEmail(tokenService.validateToken(token.replace("Bearer ", ""))).get();
        UserResponseDTO responseDTO = new UserResponseDTO(user.getUserName(), user.getEmail());
        return ResponseEntity.ok(responseDTO);
    }

    public ResponseEntity<String> setQrToken(){
        String qrToken = UUID.randomUUID().toString();
        qrTokens.add(new QrTokenDTO(qrToken, generateExpirationDate(), null));
        return ResponseEntity.ok(qrToken);
    }

    public ResponseEntity<String> matchQrToken(String token, String userToken){

        for (int i = 0; i < qrTokens.size(); i++) {

            QrTokenDTO qrToken = qrTokens.get(i);

            if (token.equals(qrToken.qrToken())) {

                QrTokenDTO updated = new QrTokenDTO(
                        qrToken.qrToken(),
                        qrToken.expiration(),
                        userToken
                );

                qrTokens.set(i, updated);

                return ResponseEntity.ok("Match feito!");
            }
        }

        return ResponseEntity.status(404).body("Token/Usuario não encontrado");
    }

    public ResponseEntity<String> getQrToken(String token){
        for( QrTokenDTO qrToken: qrTokens ){
            if(token.equals(qrToken.qrToken()) && qrToken.userToken() != null){
                String finalToken = qrToken.userToken();
                qrTokens.remove(qrToken);
                return ResponseEntity.ok(finalToken);
            }
        }
        return ResponseEntity.status(404).body("Token/Usuario não encontrado");
    }

    public ResponseEntity<List<QrTokenDTO>> getAllBitches(){
        return ResponseEntity.ok(qrTokens);
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}

