package ru.rdsystems.demo.services.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.kafka.KafkaProducer;
import ru.rdsystems.demo.model.entities.EmployeeEntity;
import ru.rdsystems.demo.model.entities.KafkaErrorMessageEntity;
import ru.rdsystems.demo.repositories.KafkaErrorMessageRepository;
import ru.rdsystems.demo.services.KafkaMessageService;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaMessageServiceImpl implements KafkaMessageService {

	private final KafkaErrorMessageRepository repository;
	private final KafkaProducer kafkaProducer;

	@Value("${kafka.topic.employeeData}")
	private String kafkaEmployeeTopic;

	@Override
	public void sendMessage(String topic, String message){
		try{
			kafkaProducer.sendMessage(topic, message);
		}catch (Exception e){
			log.error(e.getMessage());
			KafkaErrorMessageEntity errorMessage = createErrorMessageOnSend(message, topic, e.getMessage());
			log.info("Error message sended to error list, id = {}", errorMessage.getId());
		}
	}

	@Override
	public void sendMessage(EmployeeEntity employee) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String message = objectMapper.writeValueAsString(employee);
			log.info("Message to kafka {}", message);
			sendMessage(kafkaEmployeeTopic, message);
		} catch (JsonProcessingException e) {
			log.error("Ошибка упаковки в json: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	private KafkaErrorMessageEntity create(String message, String topic, String errorTxt){
		KafkaErrorMessageEntity errorMessage = new KafkaErrorMessageEntity(
				UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
				LocalDateTime.now(), null, topic, message, errorTxt
		);
		repository.save(errorMessage);
		return errorMessage;
	}

	@Override
	public KafkaErrorMessageEntity createErrorMessageOnSend(String message, String topic, String errorTxt){
		KafkaErrorMessageEntity errorMessage = repository.findByMessageAndTopicName(message, topic)
				.orElseGet(() -> create(message, topic, errorTxt));
		errorMessage.setErrorLastSentDate(LocalDateTime.now());
		return errorMessage;
	}

	@Override
	public void deleteById(String id){
		repository.deleteById(id);
	}

}
