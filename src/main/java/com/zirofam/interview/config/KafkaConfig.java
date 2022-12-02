package com.zirofam.interview.config;

import com.zirofam.interview.events.dto.FinancialDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
  private final KafkaProperties kafkaProperties;

  @Value(value = "${financial.topic-name}")
  private String topicName;

  @Bean
  public ConsumerFactory<String, FinancialDto> financialConsumerFactory() {
    Map<String, Object> props = kafkaProperties.buildConsumerProperties();
    JsonDeserializer jsonDeserializer = new JsonDeserializer(FinancialDto.class, false);
    return new DefaultKafkaConsumerFactory<>(props,
            new StringDeserializer(),
            jsonDeserializer);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, FinancialDto>
      concurrentFinancialListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, FinancialDto> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(financialConsumerFactory());
    factory.setRecordFilterStrategy(
        consumerRecord -> consumerRecord.value().getAmount().abs().doubleValue() > 1000.00);
    return factory;
  }

  @Bean
  public NewTopic financialTopic() {
    return TopicBuilder.name(topicName).partitions(10).replicas(1).build();
  }
}
