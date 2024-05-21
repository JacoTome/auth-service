package musico.services.user.config.kafka;

import org.springframework.beans.factory.annotation.Value;

//@Configuration
public class KafkaProducerConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;


//    @Bean
//    public ProducerFactory<String, AuthProcessDTO> producerFactoryAuthProcess() {
//        Map<String, Object> configProps = new HashMap<>();
//        configProps.put(
//                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                bootstrapAddress);
//        configProps.put(
//                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//                StringSerializer.class);
//        configProps.put(
//                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//                JsonSerializer.class);
//        return new DefaultKafkaProducerFactory<>(configProps);
//    }
//
//
//    @Bean
//    public KafkaTemplate<String, AuthProcessDTO> kafkaTemplateAuthProcess() {
//        return new KafkaTemplate<>(producerFactoryAuthProcess());
//    }
//
//
//    @Bean //register and configure replying kafka template
//    public ReplyingKafkaTemplate<String, AuthProcessDTO, AuthProcessDTO> replyingTemplateAuthProcess(
//            ProducerFactory<String, AuthProcessDTO> pf,
//            ConcurrentMessageListenerContainer<String, AuthProcessDTO> repliesContainer) {
//        ReplyingKafkaTemplate<String, AuthProcessDTO, AuthProcessDTO> replyTemplate = new ReplyingKafkaTemplate<>(pf, repliesContainer);
//        replyTemplate.setDefaultReplyTimeout(Duration.ofSeconds(10));
//        replyTemplate.setSharedReplyTopic(true);
//        return replyTemplate;
//    }
//    @Bean //register ConcurrentMessageListenerContainer bean
//    public ConcurrentMessageListenerContainer<String, AuthProcessDTO> repliesContainerAuthProcess(
//            ConcurrentKafkaListenerContainerFactory<String, AuthProcessDTO> containerFactory) {
//        ConcurrentMessageListenerContainer<String, AuthProcessDTO> repliesContainer = containerFactory.createContainer("auth-reply");
//        repliesContainer.getContainerProperties().setGroupId("auth-reply-group");
//        repliesContainer.setAutoStartup(false);
//        return repliesContainer;
//    }
//
}