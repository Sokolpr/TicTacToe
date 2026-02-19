package s21.peanutsh.TicTacToe.domain.service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DecodeBase64 {
    public static String[] decodeBase64(String header) throws Exception {
        if (header == null || !header.startsWith("Basic ")) {
            throw new Exception("invalid header");
        }
        String subHeader = header.substring("Basic ".length()); // отбрасываем префикс Basic
        String decode = new String(Base64.getDecoder().decode(subHeader), StandardCharsets.UTF_8); //декодируем
        return decode.split(":");
    }
}
