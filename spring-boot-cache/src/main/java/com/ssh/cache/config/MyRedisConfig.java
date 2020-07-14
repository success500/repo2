package com.ssh.cache.config;

import com.ssh.cache.bean.Employee;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
public class MyRedisConfig {


    @Configuration
    @EnableCaching
    @ConfigurationProperties(prefix = "spring.cache.redis")
    public class CacheConfiguration {
        private Duration timeToLive = Duration.ZERO;

        public void setTimeToLive(Duration timeToLive) {
            this.timeToLive = timeToLive;
        }

        //在RedisAutoConfig类中
        @Bean
        public RedisTemplate<Object, Employee> redisTemplate(RedisConnectionFactory redisConnectionFactory)
                throws UnknownHostException {
            RedisTemplate<Object, Employee> redisTemplate = new RedisTemplate<>();
            redisTemplate.setConnectionFactory(redisConnectionFactory);
            redisTemplate.setKeySerializer(keySerializer());
            redisTemplate.setHashKeySerializer(keySerializer());
            redisTemplate.setValueSerializer(valueSerializer());
            redisTemplate.setHashValueSerializer(valueSerializer());
            return redisTemplate;
        }

        @Bean
        public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
            RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(this.timeToLive)
                    .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
                    .disableCachingNullValues();

            RedisCacheManager redisCacheManager = RedisCacheManager.builder(connectionFactory)
                    .cacheDefaults(config)
                    .transactionAware()
                    .build();
            return redisCacheManager;
        }


        private RedisSerializer<String> keySerializer() {
            return new StringRedisSerializer();
        }

        private RedisSerializer<Object> valueSerializer() {
            return new GenericJackson2JsonRedisSerializer();
        }

    }

}
