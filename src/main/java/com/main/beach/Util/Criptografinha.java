package com.main.beach.Util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Criptografinha {

    private static final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public static String hash(String senha) {
        return encoder.encode(senha);
    }

    public static boolean verify(String senha, String hash) {
        return encoder.matches(senha, hash);
    }
}
