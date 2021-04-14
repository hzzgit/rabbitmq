package com.hzz.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

public class TopicSend {

    private final static String QUEUE_NAME = "topicqueue1";
    private final static String QUEUE_NAME2 = "topicqueue2";
    private final static String CHANGENAME = "topic-change";

    public static void main(String[] argv)throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("admin");
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //1、创建交换机
        channel.exchangeDeclare(CHANGENAME, BuiltinExchangeType.TOPIC,true,false,null);

        //2、创建队列
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.queueDeclare(QUEUE_NAME2,true,false,false,null);

        //3、绑定队列
        channel.queueBind(QUEUE_NAME,CHANGENAME,"error.*");
        channel.queueBind(QUEUE_NAME2,CHANGENAME,"info.#");
//        channel.queueBind(QUEUE_NAME,CHANGENAME,"error.#");

        String errmsg="error";
        //4发送消息测试
        channel.basicPublish(CHANGENAME,"error.buy",null,errmsg.getBytes());
        channel.basicPublish(CHANGENAME,"error.buy.err",null,errmsg.getBytes());

        String infomsg="info";
        //4发送消息测试
        channel.basicPublish(CHANGENAME,"info.buy.err",null,infomsg.getBytes());


        channel.close();
        connection.close();



    }
}