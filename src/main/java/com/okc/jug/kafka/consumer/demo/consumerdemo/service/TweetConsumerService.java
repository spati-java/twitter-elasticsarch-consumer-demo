package com.okc.jug.kafka.consumer.demo.consumerdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
@Slf4j
public class TweetConsumerService {

    private CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(topics = "twitter_tweet",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeAsString(ConsumerRecord<String, String> cr,
                                @Payload String payload) {
        log.info(payload);
        latch.countDown();
    }

}
