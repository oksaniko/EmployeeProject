package ru.rdsystems.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rdsystems.demo.model.EmployeeEntity;
import ru.rdsystems.demo.repositories.EmployeeRepository;
import ru.rdsystems.demo.services.EmployeeService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

	private final EmployeeService employeeService;
	private final EmployeeRepository employeeRepository;

	@GetMapping("/employees")
	public ResponseEntity<Map<String, Object>> getEmployees(){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("employees",employeeRepository.findAll()));
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Map<String, Object>> getEmployeeInfo(@PathVariable String id){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("employee", employeeService.getById(id)));
	}

	@PostMapping("/employees/{id}")
	public ResponseEntity<EmployeeEntity> createOrUpdateEmployeeById(@PathVariable String id, @RequestBody EmployeeEntity employee){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(employeeService.createOrUpdateEmployee(id, employee));
	}

}
