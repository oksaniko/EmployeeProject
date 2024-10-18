package ru.rdsystems.demo.services;

import ru.rdsystems.demo.model.entities.KafkaErrorMessageEntity;

public interface KafkaErrorMessageService {

	KafkaErrorMessageEntity createOnSend(String message, String topic, String errorTxt);

	void deleteById(String id);

}
