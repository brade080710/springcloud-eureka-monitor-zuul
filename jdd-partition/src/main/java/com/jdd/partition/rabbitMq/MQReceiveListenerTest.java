package com.jdd.partition.rabbitMq;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.jdd.partition.common.MqQueueConstant;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MQReceiveListenerTest {

    private static  RabbitTemplate rabbitTemplate;

    private ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses("115.28.136.194:5672");
        connectionFactory.setUsername("user");
        connectionFactory.setPassword("b0f63096-ea5a-43fa-81cf-554d76269e7f");
//        connectionFactory.setAddresses("192.168.2.153:5672");
//        connectionFactory.setUsername("user");
//        connectionFactory.setPassword("b0f63096-ea5a-43fa-81cf-554d76269e7f");
        connectionFactory.setVirtualHost("/");
        connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
        connectionFactory.setChannelCacheSize(25);
        connectionFactory.setChannelCheckoutTimeout(0);
        connectionFactory.setPublisherReturns(false);
        connectionFactory.setPublisherConfirms(false);
        connectionFactory.addConnectionListener(connection -> log.info("开始建立链接：{}",connection));
        connectionFactory.setConnectionLimit(Integer.MAX_VALUE);
        return connectionFactory;
    }

    public SimpleRabbitListenerContainerFactory listenerContainer() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(1);
        factory.setPrefetchCount(1);
        factory.setTxSize(1);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setErrorHandler(t -> log.error(t.getMessage(),t));
        return factory;
    }

    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMandatory(true);
        /**
         * 管理mq事务
         */
        rabbitTemplate.setChannelTransacted(false);

//        rabbitTemplate.setMessageConverter(messageConverter());
        /**
         * 设置重试机制
         */
        rabbitTemplate.setRetryTemplate(new RetryTemplate());
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> log.info("消息发送成功:correlationData({}),ack({}),cause({})", correlationData, ack, cause));
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> log.info("消息丢失:exchange({}),route({}),replyCode({}),replyText({}),message:{}", exchange, routingKey, replyCode, replyText, message));
        return rabbitTemplate;
    }


    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Before
    public void init() {
        log.info("执行初始化操作。。。");
        rabbitTemplate = rabbitTemplate();
    }

    @Test
    public void receiveMap() {
        Map msgData = new HashMap();
        msgData.put("partitionId", "1212");
//        msgData.put("cardcustId", "123");
        msgData.put("invitedCardcustId", "123456789");
        msgData.put("assistType", "1");//助力类型：1.开户，2：注册，3：助力
        msgData.put("bankType", "1");//开户银行类型：0:其他银行,1.工行
        msgData.put("userName", "zhangsan");
        msgData.put("mobile", "18737129795");
//        rabbitTemplate.convertAndSend(MqQueueConstant.SEKILL_ORDER_CANCLE_QUEUE,msgData);
        rabbitTemplate.convertAndSend(MqQueueConstant.PARTITION_PACKET_QUEUE, msgData);
    }


    @After()
    public void destory(){
        log.info("执行销毁操作。。。");
    }
}