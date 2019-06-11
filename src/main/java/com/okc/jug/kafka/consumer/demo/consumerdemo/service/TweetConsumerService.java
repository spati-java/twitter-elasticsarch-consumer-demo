package com.okc.jug.kafka.consumer.demo.consumerdemo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
@Slf4j
public class TweetConsumerService {

    private CountDownLatch latch = new CountDownLatch(1);

    private final RestHighLevelClient client;

    @KafkaListener(topics = "twitter_tweet",
            containerFactory = "kafkaListenerContainerFactory")
    public void consumeAsString(ConsumerRecord<String, String> cr,
                                @Payload String payload) {
        log.info(payload);

        sendTweetToEs(payload, cr.topic() + cr.partition() + cr.offset());

        latch.countDown();
    }

    private void sendTweetToEs(String tweet, String id) {

        try {

            IndexRequest request = new IndexRequest("tweets_techlahoma", "tweets", id)
                    .source(tweet, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            log.info(response.status().name());


        } catch (Exception e) {

            log.error(e.getMessage());
        }

    }

}
