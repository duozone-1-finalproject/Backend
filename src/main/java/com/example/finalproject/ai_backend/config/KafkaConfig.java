// 3. KafkaConfig.java 수정 (토픽명도 환경변수로)
package com.example.finalproject.ai_backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    // 토픽명도 환경변수로 설정 가능하게
    @Value("${kafka.topics.ai-request:ai-report-request}")
    private String aiRequestTopic;

    @Value("${kafka.topics.ai-response:ai-report-response}")
    private String aiResponseTopic;

    @Value("${kafka.topics.fastapi-request:fastapi-equity-request}")
    private String fastapiRequestTopic;

    @Value("${kafka.topics.fastapi-response:fastapi-equity-response}")
    private String fastapiResponseTopic;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.RETRIES_CONFIG, Integer.MAX_VALUE);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // 토픽 생성 - 환경변수 기반
    @Bean
    public NewTopic aiRequestTopicBean() {
        log.info("Creating Kafka topic: {}", aiRequestTopic);
        return TopicBuilder.name(aiRequestTopic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic aiResponseTopicBean() {
        log.info("Creating Kafka topic: {}", aiResponseTopic);
        return TopicBuilder.name(aiResponseTopic).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic fastApiEquityRequestTopicBean() {
        log.info("Creating Kafka topic for FastAPI: {}", fastapiRequestTopic);
        return TopicBuilder.name(fastapiRequestTopic).partitions(1).replicas(1).build();
    }

    @Bean
    public NewTopic fastApiEquityResponseTopicBean() {
        log.info("Creating Kafka topic for FastAPI: {}", fastapiResponseTopic);
        return TopicBuilder.name(fastapiResponseTopic).partitions(1).replicas(1).build();
    }

    // Getter 메소드들 (서비스에서 토픽명 사용할 수 있도록)
    public String getAiRequestTopic() {
        return aiRequestTopic;
    }

    public String getAiResponseTopic() {
        return aiResponseTopic;
    }

    public String getFastapiRequestTopic() {
        return fastapiRequestTopic;
    }

    public String getFastapiResponseTopic() {
        return fastapiResponseTopic;
    }
}