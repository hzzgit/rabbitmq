package com.hzz.rabbitmqspringboot.direct;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author ：hzz
 * @description：TODO
 * @date ：2021/4/14 20:54
 */
@Service
public class DirectProviceService {

    /**
     * 注入rabbitmq的发送类
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    private void init(){
             new Thread(()->{
                             while (true){
                                 sendDirect();
                                 try {
                                     Thread.sleep(10000);
                                 } catch (InterruptedException e) {
                                     e.printStackTrace();
                                 }
                             }
                     }).start();
    }

    private void sendDirect(){
        String messageId = String.valueOf(UUID.randomUUID());
        String messageData = "test message, hello!";
        String createTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Map<String,Object> map=new HashMap<>();
        map.put("messageId",messageId);
        map.put("messageData",messageData);
        map.put("createTime",createTime);
        //将消息携带绑定键值：TestDirectRouting 发送到交换机TestDirectExchange

        rabbitTemplate.convertAndSend("SpringDirectExchange", "SpringDirectRouting", map);

    }
}
