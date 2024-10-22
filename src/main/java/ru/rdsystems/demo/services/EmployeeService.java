package ru.rdsystems.demo.services;

import ru.rdsystems.demo.model.entities.EmployeeEntity;
import java.util.Map;

public interface EmployeeService {

	EmployeeEntity getById(String id);
	EmployeeEntity createOrUpdateEmployee(String id, EmployeeEntity employeeRequest);
	Map<String, Object> generateRandom(EmployeeEntity employeeRequest);

}