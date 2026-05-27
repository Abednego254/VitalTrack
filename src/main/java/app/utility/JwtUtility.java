 package app.utility;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class JwtUtility {
    private static final String SECRET_KEY = "VitalTrackSuperSecretEncryptionKeyForJWTAuthToken";
    private static final long EXPIRATION_TIME_MS = 3600000; // 1 hour

    public static String generateToken(String email, String name, String role) {
        try {
            String header = Base64.getUrlEncoder().withoutPadding().encodeToString(
                "{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8)
            );
            long now = System.currentTimeMillis();
            long exp = now + EXPIRATION_TIME_MS;
            String payloadJson = String.format(
                "{\"sub\":\"%s\",\"name\":\"%s\",\"role\":\"%s\",\"iat\":%d,\"exp\":%d}",
                email, name, role, now / 1000, exp / 1000
            );
            String payload = Base64.getUrlEncoder().withoutPadding().encodeToString(
                payloadJson.getBytes(StandardCharsets.UTF_8)
            );
            String signatureInput = header + "." + payload;
            String signature = calculateHmacSha256(signatureInput, SECRET_KEY);
            return signatureInput + "." + signature;
        } catch (Exception e) {
            throw new RuntimeException("Error generating JWT", e);
        }
    }

    public static Claims validateToken(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                return null;
            }
            String header = parts[0];
            String payload = parts[1];
            String signature = parts[2];

            String expectedSignature = calculateHmacSha256(header + "." + payload, SECRET_KEY);
            if (!expectedSignature.equals(signature)) {
                return null;
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(payload), StandardCharsets.UTF_8);
            Claims claims = parseClaims(payloadJson);
            if (claims == null) {
                return null;
            }

            // Check expiration
            if (claims.getExp() < (System.currentTimeMillis() / 1000)) {
                return null;
            }

            return claims;
        } catch (Exception e) {
            return null;
        }
    }

    private static String calculateHmacSha256(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hash = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    private static Claims parseClaims(String json) {
        try {
            String sub = extractJsonValue(json, "sub");
            String name = extractJsonValue(json, "name");
            String role = extractJsonValue(json, "role");
            String expStr = extractJsonValue(json, "exp");
            long exp = expStr != null ? Long.parseLong(expStr) : 0;
            return new Claims(sub, name, role, exp);
        } catch (Exception e) {
            return null;
        }
    }

    private static String extractJsonValue(String json, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = json.indexOf(pattern);
        if (start != -1) {
            start += pattern.length();
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        }
        pattern = "\"" + key + "\":";
        start = json.indexOf(pattern);
        if (start != -1) {
            start += pattern.length();
            int end = json.indexOf(",", start);
            if (end == -1) {
                end = json.indexOf("}", start);
            }
            return json.substring(start, end).trim();
        }
        return null;
    }

    public static class Claims {
        private final String sub;
        private final String name;
        private final String role;
        private final long exp;

        public Claims(String sub, String name, String role, long exp) {
            this.sub = sub;
            this.name = name;
            this.role = role;
            this.exp = exp;
        }

        public String getSub() { return sub; }
        public String getName() { return name; }
        public String getRole() { return role; }
        public long getExp() { return exp; }
    }
}
