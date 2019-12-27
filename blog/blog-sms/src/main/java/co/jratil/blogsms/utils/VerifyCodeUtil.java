package co.jratil.blogsms.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
public class VerifyCodeUtil {

    public static String generateCode() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9000) + 1000);
    }
}
