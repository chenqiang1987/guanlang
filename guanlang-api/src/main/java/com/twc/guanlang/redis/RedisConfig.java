//package com.twc.guanlang.redis;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//import redis.clients.jedis.JedisPoolConfig;
//
//@Configuration
//@EnableAutoConfiguration
//public class RedisConfig {
//
//    private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);
//
//    @Bean
//    @ConfigurationProperties(prefix="spring.redis")
//    public JedisPoolConfig getRedisConfig(){
//        JedisPoolConfig config = new JedisPoolConfig();
//        return config;
//    }
//
//    @Bean
//    @ConfigurationProperties(prefix="spring.redis")
//    public JedisConnectionFactory getConnectionFactory(){
//        JedisConnectionFactory factory = new JedisConnectionFactory();
//        JedisPoolConfig config = getRedisConfig();
//        factory.setPoolConfig(config);
//        logger.info("JedisConnectionFactory bean init success.");
//        return factory;
//    }
//
//    @Bean
//    public RedisTemplate<?, ?> getRedisTemplate(){
//        RedisTemplate<?,?> template = new StringRedisTemplate(getConnectionFactory()); //只能对字符串的键值操作
//        System.out.println("生成<?,?>template:" + template);
//        return template;
//    }
//
//    @Bean(name = "LongRedisTemplate")
//    public RedisTemplate<String, Long> getRedisTemplate1(){
//        RedisTemplate<String,Long> template = new RedisTemplate<String,Long>(); //只能对字符串的键值操作
//        template.setConnectionFactory(getConnectionFactory());
//
//        // 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        template.setKeySerializer(jackson2JsonRedisSerializer);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashKeySerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//
//        System.out.println("生成<String, Long>template:" + template);
//        return template;
//    }
//
//    @Bean(name = "StringRedisTemplate")
//    public RedisTemplate<String, String> getRedisTemplate2(){
//        RedisTemplate<String,String> template = new RedisTemplate<String,String>(); //只能对字符串的键值操作
//        template.setConnectionFactory(getConnectionFactory());
//
//        // 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        template.setKeySerializer(jackson2JsonRedisSerializer);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashKeySerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//
//        System.out.println("生成<String, String>template:" + template);
//        return template;
//    }
//
//    @Bean(name = "IntegerRedisTemplate")
//    public RedisTemplate<String, Integer> getRedisTemplate3(){
//        RedisTemplate<String,Integer> template = new RedisTemplate<String,Integer>(); //只能对字符串的键值操作
//        template.setConnectionFactory(getConnectionFactory());
//
//        // 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        template.setKeySerializer(jackson2JsonRedisSerializer);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashKeySerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//
//        System.out.println("生成<String,Integer>template:" + template);
//        return template;
//    }
//}
