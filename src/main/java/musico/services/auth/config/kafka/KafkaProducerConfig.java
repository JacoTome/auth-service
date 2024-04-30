package musico.services.auth.config.kafka;

import musico.services.auth.dto.AuthKafkaDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public ProducerFactory<String, AuthKafkaDTO> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, AuthKafkaDTO> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Bean //register and configure replying kafka template
    public ReplyingKafkaTemplate<String, AuthKafkaDTO, AuthKafkaDTO> replyingTemplate(
            ProducerFactory<String, AuthKafkaDTO> pf,
            ConcurrentMessageListenerContainer<String, AuthKafkaDTO> repliesContainer) {
        ReplyingKafkaTemplate<String, AuthKafkaDTO, AuthKafkaDTO> replyTemplate = new ReplyingKafkaTemplate<>(pf, repliesContainer);
        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
        replyTemplate.setSharedReplyTopic(true);
        return replyTemplate;
    }
    @Bean //register ConcurrentMessageListenerContainer bean
    public ConcurrentMessageListenerContainer<String, AuthKafkaDTO> repliesContainer(
            ConcurrentKafkaListenerContainerFactory<String, AuthKafkaDTO> containerFactory) {
        ConcurrentMessageListenerContainer<String, AuthKafkaDTO> repliesContainer = containerFactory.createContainer("auth-reply");
        repliesContainer.getContainerProperties().setGroupId("auth-reply-group");
        repliesContainer.setAutoStartup(false);
        return repliesContainer;
    }

}