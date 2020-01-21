package com.jdd.partition.config;

import com.jdd.partition.common.MqQueueConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Bean
    public Queue partitionPacketQueue() {
        return new Queue(MqQueueConstant.PARTITION_PACKET_QUEUE);
    }

}
