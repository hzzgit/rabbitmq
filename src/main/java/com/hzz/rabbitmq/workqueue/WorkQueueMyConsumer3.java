package com.hzz.rabbitmq.workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;

public class WorkQueueMyConsumer3 {

    private final static String QUEUE_NAME = "helloworkqueue";

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
        // 声明队列 参数依次 队列名称  是否持久化  是否排它(仅此连接) 是否自动删除  其他构造参数

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
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


                channel.basicAck(envelope.getDeliveryTag(),false);

                // 手动发送消息告诉给mq服务器端  从队列删除该消息,第二个参数如果是true则拒绝之前的所有消息，false则是当前消息
                //channel.basicNack(envelope.getDeliveryTag(),false,true);
            }
        };

        //  监听队列， 第二个参数false表示手动返回完成状态，true表示自动  false表示手动返回ack
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

}