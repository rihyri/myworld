package com.community.board.mail;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Random;


@Component
public class CommonUtil {

    public String markdown(String content) {
        return content;
    }


    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 10;
    private final Random random = new SecureRandom();


    public String createTempPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);
        for (int i=0; i < PASSWORD_LENGTH; i++) {
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return password.toString();
    }

}
