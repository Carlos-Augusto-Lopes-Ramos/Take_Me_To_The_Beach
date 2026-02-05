package com.main.beach.Services;

import com.main.beach.DTOS.QrStatus;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.time.Duration;
import java.util.UUID;
@Service
public class QrService {

    private static final Duration QR_TTL = Duration.ofMinutes(2);

    private final StringRedisTemplate redisTemplate;

    // token -> emitter + session
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, HttpSession> sessions = new ConcurrentHashMap<>();

    public QrService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 1️⃣ Gera o token do QR Code
     */
    public String generateQrToken() {
        String token = UUID.randomUUID().toString();

        redisTemplate.opsForValue()
                .set(buildKey(token), "PENDING", QR_TTL);

        return token;
    }

    /**
     * 2️⃣ Navegador abre conexão SSE
     */
    public SseEmitter createEmitter(String token, HttpSession session) {
        SseEmitter emitter = new SseEmitter(QR_TTL.toMillis());

        emitters.put(token, emitter);
        sessions.put(token, session);

        emitter.onCompletion(() -> cleanup(token));
        emitter.onTimeout(() -> cleanup(token));
        emitter.onError(e -> cleanup(token));

        return emitter;
    }

    /**
     * 3️⃣ Celular confirma o QR
     */
    public void confirmQr(String token, Long userId) {
        String key = buildKey(token);

        String value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new RuntimeException("QR expirado ou inválido");
        }

        redisTemplate.opsForValue()
                .set(key, "AUTHENTICATED:" + userId, QR_TTL);

        // 🔥 avisa o navegador
        SseEmitter emitter = emitters.get(token);
        HttpSession session = sessions.get(token);

        if (emitter != null && session != null) {
            session.setAttribute("USER_ID", userId);

            try {
                emitter.send(SseEmitter.event()
                        .name("AUTHENTICATED")
                        .data(userId));
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            } finally {
                cleanup(token);
            }
        }
    }

    private void cleanup(String token) {
        emitters.remove(token);
        sessions.remove(token);
        redisTemplate.delete(buildKey(token));
    }

    private String buildKey(String token) {
        return "qr:" + token;
    }
}

