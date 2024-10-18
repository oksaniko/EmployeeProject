package ru.rdsystems.demo.services.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.model.entities.KafkaErrorMessageEntity;
import ru.rdsystems.demo.repositories.KafkaErrorMessageRepository;
import ru.rdsystems.demo.services.KafkaErrorMessageService;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class KafkaErrorMessageServiceImpl implements KafkaErrorMessageService {

	private final KafkaErrorMessageRepository repository;

	private KafkaErrorMessageEntity create(String message, String topic, String errorTxt){
		KafkaErrorMessageEntity errorMessage = new KafkaErrorMessageEntity(
				UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
				LocalDateTime.now(), null, topic, message, errorTxt
		);
		repository.save(errorMessage);
		return errorMessage;
	}

	@Override
	public KafkaErrorMessageEntity createOnSend(String message, String topic, String errorTxt){
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
