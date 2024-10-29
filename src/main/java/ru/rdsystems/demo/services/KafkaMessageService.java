package ru.rdsystems.demo.services;

import ru.rdsystems.demo.model.entities.EmployeeEntity;
import ru.rdsystems.demo.model.entities.KafkaErrorMessageEntity;

public interface KafkaMessageService {

	KafkaErrorMessageEntity createErrorMessageOnSend(String message, String topic, String errorTxt);
	void deleteById(String id);
	void sendMessage(String topic, String message);
	void sendMessage(EmployeeEntity employee);

}
