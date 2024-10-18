package ru.rdsystems.demo.services;

import ru.rdsystems.demo.model.entities.EmployeeEntity;

public interface EmployeeService {

	EmployeeEntity getById(String id);
	EmployeeEntity createOrUpdateEmployee(String id, EmployeeEntity employeeRequest);

}