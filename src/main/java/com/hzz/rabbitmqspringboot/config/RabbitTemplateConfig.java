package com.hzz.rabbitmqspringboot.config;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


/**
 * @author ：hzz
 * @description：这边是将my的发送改为多例注入，这样从工厂里面拿就是多例
 * @date ：2021/4/14 21:45
 */
@Configuration
public class RabbitTemplateConfig {



    @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMandatory(true);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }


}
