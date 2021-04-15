package com.hzz.rabbitmqspringboot.TTL死信demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * rabbitMq生产者类
 * @author zhanghang
 * @date 2018/12/13
 */
@Component
@Slf4j
public class RabbitProduct {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendDelayMessage(List<Integer> list) {
        //这里的消息可以是任意对象，无需额外配置，直接传即可
        log.info("===============延时队列生产消息====================");
        log.info("发送时间:{},发送内容:{}", LocalDateTime.now(), list.toString());
//        this.rabbitTemplate.convertAndSend(
//                "delay_exchange",
//                "delay_key",
//                list,
//                message -> {
//                    //这边是设置单条消息的过期时间，和队列过期时间取最小值
//                    //这边要注意只有在队列的第一条才会让过期的消息消失掉
//                    //注意这里时间要是字符串形式
//                    message.getMessageProperties().setExpiration("6000");
//                    return message;
//                }
//        );
        rabbitTemplate.convertAndSend("delay_exchange","delay_key",list);
        log.info("{}ms后执行", 6000);
    }


}