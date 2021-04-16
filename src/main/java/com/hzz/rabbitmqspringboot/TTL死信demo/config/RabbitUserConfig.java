package com.hzz.rabbitmqspringboot.TTL死信demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitUserConfig {
    /**
     * 死信交换机
     * @return
     */
    @Bean
    public DirectExchange delayExchange(){
        return new DirectExchange("delay_exchange");
    }

    /**
     * 死信队列
     * @return
     */
    @Bean
    public Queue delayQueue(){
        Map<String,Object> map = new HashMap<>(16);
        //这边是绑定死信队列，其实这个的原理就是当多久没有收到这个队列的消息，那么这个队列这个消息就会发送到死信交换机
        map.put("x-dead-letter-exchange","receive_exchange");
        //这边是绑定要发送到的routeing key，其实就是发送到死信队列，最后的消费者只要接收死信队列就可以直接获取到
        map.put("x-dead-letter-routing-key", "receive_key");

        //设置队列的过期时间，一般来说这个用来做延迟队列，配合死信队列即可实现
        map.put("x-message-ttl",30000);

        //设置队列的最大长度，死信队列第二个条件，达到队列最大长度则发送到死信交换机
        map.put("x-max-length",100);

        //死信队列第三个条件是当消费者手动签收拒签的时候 ，并且不回到队列的最上头，则会发送到死信交换机

        //队列的最后一个参数就是rabbitMq管理界面里面Arguments参数对应的值
        return new Queue("delay_queue",true,false,false,map);
    }

    /**
     * 给死信队列绑定交换机
     * @return
     */
    @Bean
    public Binding delayBinding(Queue delayQueue, DirectExchange delayExchange){
        return BindingBuilder.bind(delayQueue).to(delayExchange).with("delay_key");
    }

    /**
     * 死信接收交换机
     * @return
     */
    @Bean
    public DirectExchange receiveExchange(){
        return new DirectExchange("receive_exchange");
    }

    /**
     * 死信接收队列
     * @return
     */
    @Bean
    public Queue receiveQueue(){
        return new Queue("receive_queue");
    }

    /**
     * 死信交换机绑定消费队列
     * @return
     */
    @Bean
    public Binding receiveBinding(Queue receiveQueue,DirectExchange receiveExchange){
        return BindingBuilder.bind(receiveQueue).to(receiveExchange).with("receive_key");
    }
}
