package com.twc.guanlang.mq;

import com.rabbitmq.client.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;


@SpringBootApplication
@EnableScheduling
public class RabbitMQModule {

    //队列名称
    public final static String QUEUE_NAME = "q_test_01";

    public static void main(String arg[]) throws IOException, InterruptedException {


        SpringApplication.run(RabbitMQModule.class, arg);
        //监听简单队列消费
        simpleMQ();
    }


    //简单队列
    public static void simpleMQ() throws IOException, InterruptedException {
        // send();
        recv();

    }



    //接收消息
    public static void recv() throws IOException, InterruptedException {

        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        //声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //定义队列的消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME, true, queueingConsumer);
        //获取消息
        while (true) {
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("  receive msg: " + msg);
        }

    }


    //创建mq连接
    public static Connection getConnection() throws IOException {

        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost("192.168.1.130");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setUsername("guest");
        factory.setPassword("guest");

        Connection connection = factory.newConnection();
        return connection;
    }

}
