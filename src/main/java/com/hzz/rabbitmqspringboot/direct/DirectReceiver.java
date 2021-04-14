package com.hzz.rabbitmqspringboot.direct;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

/**
 * @author ：hzz
 * @description：consumer ack机制
 * 1、设置手动签收
 * @date ：2021/4/14 20:58
 */
@Component
public class DirectReceiver {

    @RabbitListener(queues = "SpringDirectQueue")////监听的队列名称 TestDirectQueue
    @RabbitHandler
    public void process(Message message, Channel channel) throws IOException, ClassNotFoundException {

        // 获取消息Id，用消息ID做业务判断
        String messageId = message.getMessageProperties().getMessageId();
        ByteArrayInputStream byteInt=new ByteArrayInputStream(message.getBody());
        ObjectInputStream objInt=new ObjectInputStream(byteInt);

        Map<String, Object> result = (Map<String, Object>) objInt.readObject();//byte[]转map
        System.out.println("接收到邮件队列消息：" + result + "，消息ID：" + messageId);
        // 手动签收
      //  channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);


        //这边是丢弃并且回到队列第一位
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,true);

    }
}
