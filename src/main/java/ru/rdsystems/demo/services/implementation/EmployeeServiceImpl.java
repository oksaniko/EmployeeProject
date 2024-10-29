package ru.rdsystems.demo.services.implementation;

import io.micrometer.core.instrument.Counter;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.model.entities.EmployeeEntity;
import ru.rdsystems.demo.model.enums.Status;
import ru.rdsystems.demo.remote.RandomUserClient;
import ru.rdsystems.demo.repositories.EmployeeRepository;
import ru.rdsystems.demo.services.EmployeeService;
import ru.rdsystems.demo.services.KafkaMessageService;
import ru.rdsystems.demo.services.MetricService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository repository;
	private final RandomUserClient remoteClient;
	private final MetricService metricService;
	private final Counter counter;
	private final KafkaMessageService kafkaService;

	@Value("${metrics.employees_count.name}")
	private String metricEmployeesCount;

	@Override
	public EmployeeEntity getById(String id){
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Сотрудник (id = " + id + ") не найден"));
	}

	private EmployeeEntity createEmployeeByRequest(EmployeeEntity employeeRequest){
		metricService.increment(metricEmployeesCount);
		return new EmployeeEntity(
				UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
				employeeRequest.getName(), employeeRequest.getPosition(),
				Status.WORKING, null
		);
	}

	private EmployeeEntity updateEmployeeByRequest(EmployeeEntity employeeDB, EmployeeEntity employeeRequest){
		if(employeeDB.getRandomData() == null){
			if(employeeRequest.getName() != null)
				employeeDB.setName(employeeRequest.getName());
			if(employeeRequest.getPosition() != null)
				employeeDB.setPosition(employeeRequest.getPosition());
			if(employeeRequest.getStatus() != null)
				employeeDB.setStatus(employeeRequest.getStatus());
			kafkaService.sendMessage(employeeDB);
		} else {
			if(employeeRequest.getStatus() != null && employeeRequest.getStatus().equals(Status.DELETED)){
				employeeDB.setStatus(employeeRequest.getStatus());
				kafkaService.sendMessage(employeeDB);
			}
		}
		return employeeDB;
	}

	@Override
	@Transactional
	public EmployeeEntity createOrUpdateEmployee(String id, EmployeeEntity employeeRequest) {
		EmployeeEntity employee;
		try{
			employee = updateEmployeeByRequest(getById(id), employeeRequest);
		} catch (EntityNotFoundException notFoundException){
			employee = createEmployeeByRequest(employeeRequest);
			repository.save(employee);
			kafkaService.sendMessage(employee);
		}
		return employee;
	}

	@Override
	@Transactional
	public Map<String, Object> generateRandom(EmployeeEntity employeeRequest) {
		Map<String, Object> resultMap = new HashMap<>();
		if (employeeRequest.getRandomData() == null){
			metricService.increment(counter);
			ResponseEntity<Map<String, Object>> randomResponse = remoteClient.getRandomInfo();
			if(randomResponse.getStatusCode().is2xxSuccessful()){
				resultMap = randomResponse.getBody();
				log.info(resultMap.toString());
				employeeRequest.setRandomData(new JSONObject(resultMap).toString());
				kafkaService.sendMessage(employeeRequest);
			}
		} else
			resultMap = Map.of("randomData",employeeRequest.getRandomData());
		return resultMap;
	}

}
