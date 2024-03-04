package by.clevertec.news.config;//package by.clevertec.news.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.CacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import redis.clients.jedis.JedisPoolConfig;
//
//@Configuration
//@Profile("redis")
//public class RedisCacheConfig {
//
//    @Value("${redis.port")
//    private Integer port;
//
//    @Value("${redis.host}")
//    private String host;
//
//
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
////        RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration(host, Integer.parseInt(port));
//        RedisStandaloneConfiguration redisStandaloneConfig = new RedisStandaloneConfiguration(host, port);
//
////        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(redisStandaloneConfig);
//
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(10);
//        poolConfig.setMaxIdle(5);
//
////        jedisConnectionFactory.setPoolConfig(poolConfig);
//
//        JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder()
//                .usePooling()
//                .poolConfig(poolConfig)
//                .build();
//
//        return new JedisConnectionFactory(redisStandaloneConfig, jedisClientConfiguration);
//    }
//
////    @Bean
////    public RedisConnectionFactory redisConnectionFactory() {
////        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
////        jedisConnectionFactory.setHostName(host);
////        jedisConnectionFactory.setPort(Integer.parseInt(port));
//////        jedisConnectionFactory.setHostName("localhost");
//////        jedisConnectionFactory.setPort(6379);
////        return jedisConnectionFactory;
////    }
//
////    @Bean
////    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
////        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
////                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
////
////        return RedisCacheManager.builder(redisConnectionFactory)
////                .cacheDefaults(cacheConfiguration)
////                .build();
////    }
////
////    @Bean
////    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
////        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
////        redisTemplate.setConnectionFactory(redisConnectionFactory);
////        return redisTemplate;
////    }
////
////    @Bean
////    public RedisConnectionFactory redisConnectionFactory() {
////        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration("localhost", 6379);
////
////        return new LettuceConnectionFactory(config);
////        }
////    @ConditionalOnProperty(value = "redis.cache.enabled", havingValue = "true", matchIfMissing = true)
//    @Bean
//    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        return RedisCacheManager.create(redisConnectionFactory);
//    }
//}
