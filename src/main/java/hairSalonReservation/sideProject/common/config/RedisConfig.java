package hairSalonReservation.sideProject.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${redis.host")
    private String host;

    @Value("${redis.port")
    private int port;

    @Bean
    public RedisConfiguration defaultRedisConfiguration() {

        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();

        redisConfig.setHostName(host);
        redisConfig.setPort(port);

        return redisConfig;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisConfiguration redisConfiguration) {
        LettuceClientConfiguration lettuceClientConfig = LettuceClientConfiguration.builder().build();
        return new LettuceConnectionFactory(redisConfiguration, lettuceClientConfig);
    }

    @Bean
    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());

        return redisTemplate;
    }


}
