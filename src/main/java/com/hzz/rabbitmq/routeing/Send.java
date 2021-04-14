package com.hzz.rabbitmq.routeing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

public class Send {

    private final static String QUEUE_NAME = "directqueue1";
    private final static String QUEUE_NAME2 = "directqueue2";
    private final static String CHANGENAME = "direct-change";


    public static void main(String[] argv) throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //1、创建交换机,这边是创建路由交换机
        channel.exchangeDeclare(CHANGENAME, BuiltinExchangeType.DIRECT, true, false, null);
        //2、创建队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueDeclare(QUEUE_NAME2, true, false, false, null);

        //3、绑定交换机和队列，以及写入routeingkey
        channel.queueBind(QUEUE_NAME, CHANGENAME, "error");
        channel.queueBind(QUEUE_NAME2, CHANGENAME, "error");
        channel.queueBind(QUEUE_NAME2, CHANGENAME, "info");
        channel.queueBind(QUEUE_NAME2, CHANGENAME, "debug");
        //工作任务模式，那么就是一次性往一个队列里面发很多，然后多个消费者进行消费

        String message = "error";
        channel.basicPublish(CHANGENAME, "error", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        for (int i = 0; i <100 ; i++) {
            String infomessage = "info"+i;
            channel.basicPublish(CHANGENAME, "info", null, infomessage.getBytes());
            System.out.println(" [x] Sent '" + infomessage + "'");
        }



        channel.close();
        connection.close();
    }
}