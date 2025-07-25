    package com.quyet.superapp.service;

    import lombok.RequiredArgsConstructor;
    import org.springframework.data.redis.core.RedisTemplate;
    import org.springframework.stereotype.Service;

    import java.time.Duration;

    @Service
    @RequiredArgsConstructor
    public class RedisTokenService {

        private final RedisTemplate<String, String> redisTemplate;
        private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(7);

        public void saveRefreshToken(Long userId, String token) {
            redisTemplate.opsForValue().set(buildKey(userId), token, REFRESH_TOKEN_TTL);
        }

        public boolean isValidRefreshToken(Long userId, String token) {
            String saved = redisTemplate.opsForValue().get(buildKey(userId));
            return token.equals(saved);
        }

        public void deleteRefreshToken(Long userId) {
            redisTemplate.delete(buildKey(userId));
        }

        private String buildKey(Long userId) {
            return "refresh-token:" + userId;
        }



    }
