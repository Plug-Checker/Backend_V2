package com.plugchecker.backend.domain.redis;

import com.plugchecker.backend.ServiceTest;
import com.plugchecker.backend.global.redis.EmbeddedRedisConfig;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import redis.embedded.RedisServer;

import java.lang.reflect.Field;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmbeddedRedisConfigTest extends ServiceTest {

    @Mock
    private RedisServer redisServer;

    @InjectMocks
    private EmbeddedRedisConfig embeddedRedisConfig;

    @Test
    void stopRedis_Test() throws Exception {
        // given
        Field field = EmbeddedRedisConfig.class.getDeclaredField("redisServer");
        field.setAccessible(true);
        field.set(null, redisServer);

        // when
        embeddedRedisConfig.stopRedis();

        // then
        verify(redisServer).stop();
    }

    @Test
    void notStopRedis_Test() throws Exception {
        // given
        Field field = EmbeddedRedisConfig.class.getDeclaredField("redisServer");
        field.setAccessible(true);
        field.set(null, null);

        // when
        embeddedRedisConfig.stopRedis();

        // then
        verify(redisServer, times(0)).stop();
    }
}
