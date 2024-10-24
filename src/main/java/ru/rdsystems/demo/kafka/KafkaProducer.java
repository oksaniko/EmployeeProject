package ru.rdsystems.demo.kafka;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.entities.KafkaErrorMessageEntity;
import ru.rdsystems.demo.services.KafkaErrorMessageService;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

	private final KafkaErrorMessageService service;
	private final MeterRegistry meterRegistry;

	@Value("${kafka.metrics.sended_messages_count.name}")
	private String metricSendedMessagesCount;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public boolean sendMessage(String topic, String message){
		boolean result = false;
		try {
			log.info("Sending message='{}' to topic='{}'", message, topic);
			kafkaTemplate.send(topic, message);
			meterRegistry.counter(metricSendedMessagesCount).increment();
			result = true;
		} catch (Exception e){
			log.error(e.getMessage());
			KafkaErrorMessageEntity errorMessage = service.createOnSend(message, topic, e.getMessage());
			log.info("Error message sended to error list, id = {}", errorMessage.getId());
		}
		return result;
	}

}
