package com.hzz.rabbitmq.routeing;

import com.rabbitmq.client.*;

import java.io.IOException;

public class RouteingConsumererror2 {

    private final static String QUEUE_NAME = "directqueue1";
    private final static String QUEUE_NAME2 = "directqueue2";

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

        //一次只读一条
        channel.basicQos(1);


        DefaultConsumer defaultConsumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("接收到"+new String(body));
            }
        };

        channel.basicConsume(QUEUE_NAME2,true,defaultConsumer);
    }

}