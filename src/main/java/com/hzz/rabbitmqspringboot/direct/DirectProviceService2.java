package com.hzz.rabbitmqspringboot.direct;

import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
public class DirectProviceService2 {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    private void init(){
        /**
         * 开启回调函数，这个意思是连交换机都没有发送到的情况
         * 步骤:
         *     1、配置yml文件中
         *         publisher-returns: true
         * #    这边听说新版要配置这个才能提示成功回调的消息
         *     publisher-confirm-type: correlated
         *
         *     2、rabbitTemplate需要设置成多例才可以进行多个交换机的回调，
         *     3、编写交换机发送失败的回调方法
         *
         */
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {

                System.out.println("回调函数被执行了,ack="+ack+",cause="+cause);
            }
        });


        /**
         * 开启回退模式，回退模式概念：当发送到队列失败的时候就会回退，也有可能是队列不存在
         * 步骤:
         *   1、开启回退模式，新版本好像不用配置，旧版本需要配置publicsher-returns=true
         *   2、配置rabbitTemplate中的setReturnsCallback,也就是回退函数
         *   3、设置交换机exchange处理消息的模式
         *       (1)默认交换机是会直接丢弃这消息
         *       (2)如果没有路由到queue，则返回消息给发送方的returnedMessage回调方法中 去
         */


        //设置交换机处理失败消息的模式，这边如果是true就是说明可以将发送到queue的消息进行回退通知到发送端
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            /**
             * message消息数据
             * replycode 错误码
             * replayText 错误原因
             * exchange 交换机名称
             * routeingkey 路由键
             *
             * @param returned
             */
            @Override
            public void returnedMessage(ReturnedMessage returned) {
                System.out.println("回退消息="+returned);
                //这边要进行处理，一般来说是将失败的消息，放到其他的交换机或者是路由键但找工作
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

        rabbitTemplate.convertAndSend("SpringDirectExchange", "SpringDirectRouting11", map);

    }
}
