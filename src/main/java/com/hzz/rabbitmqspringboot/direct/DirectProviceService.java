package com.hzz.rabbitmqspringboot.direct;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

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
     * 这边注入的是一个单例的rabbit发送类
     */
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private ApplicationContext applicationContext;

   // @PostConstruct
    private void init(){
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                /**
                 * correlationData这个可以在发送的时候添加进去，回调的时候可以拿到数据
                 * ack，true代表交换机已经收到了这个数据
                 * cause，失败的原因
                 */
                System.out.println("回调函数被执行了,ack="+ack+",cause="+cause);
            }
        });

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
       // RabbitTemplate rabbitTemplate=applicationContext.getBean(RabbitTemplate.class);
        /**
         * 定义发送端的回调函数
         */


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
