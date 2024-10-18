package ru.rdsystems.demo.services.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.kafka.KafkaProducer;
import ru.rdsystems.demo.model.entities.EmployeeEntity;
import ru.rdsystems.demo.model.enums.Status;
import ru.rdsystems.demo.repositories.EmployeeRepository;
import ru.rdsystems.demo.services.EmployeeService;

import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository repository;
	private final KafkaProducer kafkaProducer;

	@Value("${kafka.topic.employeeData}")
	private String kafkaTopic;

	@Override
	public EmployeeEntity getById(String id){
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Сотрудник (id = " + id + ") не найден"));
	}

	private void sendToKafka(EmployeeEntity employee){
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String message = objectMapper.writeValueAsString(employee);
			log.info("Message to kafka {}", message);
			kafkaProducer.sendMessage(kafkaTopic, message);
		} catch (JsonProcessingException e) {
			log.error("Ошибка упаковки в json: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public EmployeeEntity createOrUpdateEmployee(String id, EmployeeEntity employeeRequest) {
		EmployeeEntity employee = null;
		try{
			employee = getById(id);
			// здесь должна быть проверка на дто
			// наверное переделать
			if(!employeeRequest.getName().isEmpty())
				employee.setName(employeeRequest.getName());
			if(!(employeeRequest.getPosition() == null))
				employee.setPosition(employeeRequest.getPosition());
			if(!(employeeRequest.getStatus() == null))
				employee.setStatus(employeeRequest.getStatus());
		} catch (EntityNotFoundException notFoundException){
			employee = new EmployeeEntity(
					UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
					employeeRequest.getName(), employeeRequest.getPosition(),
					Status.WORKING, null
			);
		}
		repository.save(employee);
		sendToKafka(employee);
		return employee;
	}

}
