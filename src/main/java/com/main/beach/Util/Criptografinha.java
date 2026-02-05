package com.main.beach.Util;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

public class Criptografinha {

    private static final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder();

    public static String hash(String senha) {
        return encoder.encode(senha);
    }

    public static String generateHashToken() {
        String[] words = {
                "Java", "Best", "language", "Banana", "Boat", "MyPopstar", "Donk", "*DDont", "Donuts",
                "Redis", "Docker", "API", "Backend", "PostgreSQL", "Cloud", "Sword", "Master"
        };

        return encoder.encode(generateAStk(words, 1, 4));
    }

    public static String generateAStk(String[] words, int min, int max) {
        Random random = new Random();

        int sizeFrase = random.nextInt(max - min + 1) + min;

        StringBuilder frase = new StringBuilder();

        for (int i = 0; i < sizeFrase; i++) {
            String word = words[random.nextInt(words.length)];
            frase.append(word);

            if (i < sizeFrase - 1) {
                frase.append(" ");
            }
        }

        return frase.toString();
    }
    public static boolean verify(String senha, String hash) {
        return encoder.matches(senha, hash);
    }
}
