package ru.rdsystems.demo.services.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rdsystems.demo.kafka.KafkaProducer;
import ru.rdsystems.demo.model.entities.EmployeeEntity;
import ru.rdsystems.demo.remote.RandomUserClient;
import ru.rdsystems.demo.repositories.EmployeeRepository;
import ru.rdsystems.demo.services.EmployeeService;
import ru.rdsystems.demo.services.MetricService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
class EmployeeServiceImplTest {

	private EmployeeService employeeService;
	private EmployeeRepository repository;
	private KafkaProducer producer;
	private RandomUserClient remoteClient;
	private MetricService metricService;

	@BeforeEach
	void setUp() {
		repository = mock(EmployeeRepository.class);
		producer = mock(KafkaProducer.class);
		metricService = mock(MetricService.class);

		employeeService = new EmployeeServiceImpl(repository, producer, remoteClient, metricService);
	}

	@Test
	void getById() throws JsonProcessingException {
		// Дано
		EmployeeEntity employee = Instancio.create(EmployeeEntity.class);
		log.info("instancio: {}",new ObjectMapper().writeValueAsString(employee));
		when(repository.findById(anyString())).thenReturn(Optional.of(employee));
		// Когда
		EmployeeEntity employeeDB = employeeService.getById("testId");
		// Тогда
		log.info("DB: {}",new ObjectMapper().writeValueAsString(employeeDB));
		assertThat(employeeDB)
				.usingRecursiveComparison()
				.isEqualTo(employee);
	}

	@Test
	void createOrUpdateEmployee() throws JsonProcessingException {
		// Дано
		EmployeeEntity employee = Instancio.create(EmployeeEntity.class);
		log.info("instancio: {}",new ObjectMapper().writeValueAsString(employee));
		// Когда
		EmployeeEntity employeeCheck = employeeService.createOrUpdateEmployee(employee.getId(), employee);
		log.info("check: {}",new ObjectMapper().writeValueAsString(employeeCheck));
		// Тогда
		verify(repository, atLeastOnce()).findById(employee.getId());
		assertThat(employeeCheck)
				.usingRecursiveComparison()
				.ignoringFields("id","status","randomData")
				.isEqualTo(employee);
	}

}