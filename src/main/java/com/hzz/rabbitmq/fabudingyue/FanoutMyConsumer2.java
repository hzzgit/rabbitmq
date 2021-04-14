package com.hzz.rabbitmq.fabudingyue;

import com.rabbitmq.client.*;

import java.io.IOException;

public class FanoutMyConsumer2 {

    private final static String QUEUE_NAME = "guangbo1queue";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setConnectionTimeout(600000); // in milliseconds
        factory.setRequestedHeartbeat(60); // in seconds
        factory.setHandshakeTimeout(6000); // in milliseconds
        factory.setRequestedChannelMax(5);
        factory.setNetworkRecoveryInterval(500);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("guangbo", BuiltinExchangeType.FANOUT,true);
        //定义交换机模式  // 声明exchange     fanout广播模式    redirect 路由模式    topic 主题模式
        // 声明队列  参数依次 队列名称  是否持久化  是否排它(仅此连接) 是否自动删除  其他构造参数
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        // 绑定队列到交换机
        channel.queueBind(QUEUE_NAME, "guangbo", "");

        System.out.println("Waiting for messages. ");
        //这边是一次只读一条
         channel.basicQos(1);
        //这边是读取之后的回调函数
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true,consumer);
    }

}