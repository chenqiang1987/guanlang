//package com.twc.guanlang.mq;
//
//import com.rabbitmq.client.Channel;
//import com.rabbitmq.client.Connection;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.io.IOException;
//
//
///**
// * 生产消息
// */
//@Configuration
//public class Provider {
//
//    public Provider() throws IOException {
//        Connection connection = RabbitMQModule.getConnection();
//        Channel channel = connection.createChannel();
//
//        //创建队列
//        channel.queueDeclare(RabbitMQModule.QUEUE_NAME, false, false, false, null);
//
//        String msg = "hello world";
//        channel.basicPublish("", RabbitMQModule.QUEUE_NAME, null, msg.getBytes());
//        channel.close();
//        connection.close();
//
//    }
//
//    @Bean
//    @Scheduled(cron = "0/5 * * * * *")
//    public static void send() throws IOException {
////
////        Connection connection = RabbitMQModule.getConnection();
////        Channel channel = connection.createChannel();
////
////        //创建队列
////        channel.queueDeclare(RabbitMQModule.QUEUE_NAME, false, false, false, null);
////
////        String msg = "hello world";
////        channel.basicPublish("", RabbitMQModule.QUEUE_NAME, null, msg.getBytes());
////        channel.close();
////        connection.close();
//        //       new Provider();
//    }
//
//    public static void main(String s[]) throws IOException {
//        Connection connection = RabbitMQModule.getConnection();
//        Channel channel = connection.createChannel();
//
//        //创建队列
//        channel.queueDeclare(RabbitMQModule.QUEUE_NAME, false, false, false, null);
//
//        String msg = "hello world";
//        channel.basicPublish("", RabbitMQModule.QUEUE_NAME, null, msg.getBytes());
//        channel.close();
//        connection.close();
//
//
//    }
//
//}
