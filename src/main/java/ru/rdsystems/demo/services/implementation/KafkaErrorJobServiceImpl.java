package ru.rdsystems.demo.services.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.kafka.KafkaProducer;
import ru.rdsystems.demo.model.entities.KafkaErrorMessageEntity;
import ru.rdsystems.demo.repositories.KafkaErrorMessageRepository;
import ru.rdsystems.demo.services.KafkaErrorJobService;
import ru.rdsystems.demo.services.KafkaErrorMessageService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaErrorJobServiceImpl implements KafkaErrorJobService {

	private final KafkaErrorMessageRepository repository;
	private final KafkaErrorMessageService service;
	private final KafkaProducer producer;

	@Override
	@Scheduled(cron = "${cron.kafka.error}")
	public void repeatSendToKafka() {
		List<KafkaErrorMessageEntity> errorList = repository.findAll();
		if(errorList.isEmpty())
			log.info("No message for repeat sending");
		else
			for(KafkaErrorMessageEntity message : errorList) {
				if(producer.sendMessage(message.getTopicName(), message.getMessage())){
					service.deleteById(message.getId());
					log.info("Message '{}' send", message);
				}
			}
	}

}
