package com.hzz.rabbitmqspringboot.direct;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ：hzz
 * @description：TODO
 * @date ：2021/4/14 20:58
 */
@Component
@RabbitListener(queues = "SpringDirectQueue")////监听的队列名称 TestDirectQueue
public class DirectReceiver {

    @RabbitHandler
    public void process(Map data){
        System.out.println("DirectReceiver消费者收到消息  : " + data.toString());
    }
}
