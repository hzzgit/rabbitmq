package com.hzz.rabbitmq.fabudingyue;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv)throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
         /*
           声明exchange
           fanout广播模式  把接收到的消息推送给所有它知道的队列
           direct 路由模式
           topic 主题模式
         */
        channel.exchangeDeclare("guangbo", BuiltinExchangeType.FANOUT,true);
        //工作任务模式，那么就是一次性往一个队列里面发很多，然后多个消费者进行消费

            String message = "Hello World!";
            channel.basicPublish("guangbo", "", null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");


        channel.close();
        connection.close();
    }
}