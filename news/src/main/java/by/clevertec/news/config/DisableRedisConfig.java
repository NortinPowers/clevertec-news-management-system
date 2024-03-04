package by.clevertec.news.config;//package by.clevertec.news.config;
//
//import org.springframework.boot.actuate.health.HealthContributor;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.convert.ReferenceResolver;
//import org.springframework.data.redis.core.convert.ReferenceResolverImpl;
//
//@Configuration
//@AutoConfigureAfter(RedisAutoConfiguration.class)
//@Profile("lfu-lru")
//public class DisableRedisConfig {
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        return null;
//    }
//
//    @Bean
//    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        return null;
//    }
//
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        return null;
//    }
//
//    @Bean
//    public HealthContributor redisHealthContributor(RedisConnectionFactory redisConnectionFactory) {
//        return null;
//    }
//
//    @Bean
//    public ReferenceResolver referenceResolver(RedisOperations<?, ?> redisOperations) {
//        return new ReferenceResolverImpl(redisOperations);
//    }
//
//    @Bean
//    public RedisOperations<?, ?> redisOperations(RedisConnectionFactory redisConnectionFactory) {
//        return new RedisTemplate<>();
//    }
//}
