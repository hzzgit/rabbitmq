package com.hzz.rabbitmqspringboot.direct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.stereotype.Service;

/**
 * @author ：hzz
 * @description：TODO
 * @date ：2021/4/15 14:20
 */
@Service
public class RabbitListenerErrorHandlercom implements RabbitListenerErrorHandler {
    @Override
    public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {



        return null;
    }
}
