package ru.rdsystems.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.entities.KafkaErrorMessageEntity;
import ru.rdsystems.demo.services.KafkaMessageService;
import ru.rdsystems.demo.services.MetricService;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

	private final MetricService metricService;

	@Value("${kafka.metrics.sended_messages_count.name}")
	private String metricSendedMessagesCount;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(String topic, String message){
		log.info("Sending message='{}' to topic='{}'", message, topic);
		kafkaTemplate.send(topic, message);
		metricService.increment(metricSendedMessagesCount);
	}

}
