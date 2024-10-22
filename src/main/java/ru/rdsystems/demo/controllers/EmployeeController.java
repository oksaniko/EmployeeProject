package ru.rdsystems.demo.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rdsystems.demo.model.entities.EmployeeEntity;
import ru.rdsystems.demo.model.mappers.EmployeeMapper;
import ru.rdsystems.demo.repositories.EmployeeRepository;
import ru.rdsystems.demo.services.EmployeeService;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

	private final EmployeeService service;
	private final EmployeeRepository repository;
	private final EmployeeMapper mapper;

	@GetMapping
	public ResponseEntity<Map<String, Object>> getEmployees(){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("employees", repository.findAll()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Map<String, Object>> getEmployeeInfo(@PathVariable String id){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("employee", service.getById(id)));
	}

	@PostMapping("/{id}")
	public ResponseEntity<EmployeeEntity> createOrUpdateEmployeeById(@PathVariable String id, @RequestBody EmployeeEntity employee){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(service.createOrUpdateEmployee(id, employee));
	}

	@PostMapping("/{id}/dto")
	public ResponseEntity<Map<String, Object>> createDto(@PathVariable String id, @RequestBody Object body){
		EmployeeEntity employee = service.getById(id);
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of(
						"firstPart", service.generateRandom(employee),
						"secondPart", mapper.map(employee),
						"thirdPart", body.toString()
				));
	}

}
