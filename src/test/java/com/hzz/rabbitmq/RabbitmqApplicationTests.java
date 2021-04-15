package com.hzz.rabbitmq;

import com.hzz.RabbitmqApplication;
import com.hzz.rabbitmqspringboot.TTL死信demo.service.RabbitProduct;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitmqApplication.class)
class RabbitmqApplicationTests {

    @Autowired
    private RabbitProduct rabbitProduct;

    /**
     * 测试死信队列加延迟队列
     */
    @Test
    void testdeadinfo() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

            while (true) {
                rabbitProduct.sendDelayMessage(list);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


    }

}
