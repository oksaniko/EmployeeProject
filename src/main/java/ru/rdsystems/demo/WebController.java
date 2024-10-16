package ru.rdsystems.demo;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WebController {

	@GetMapping("/employees")
	public ResponseEntity<Map<String, Object>> getEmployees(){
		return ResponseEntity.status(HttpStatus.OK)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Map.of("employees",/*employeeRepository.findAll()*/"hello"));
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Map<String, String>> getEmployeeInfo(@PathVariable String id){
		HttpStatus responseStatus;
		Map<String, String> json = new HashMap<>();
		try{
			//EmployeeEntity employee = employeeService.getById(id);
			responseStatus = HttpStatus.OK;
			json = Map.of("employee", /*employee.toString()*/"hello");
		} catch (EntityNotFoundException e){
			responseStatus = HttpStatus.NOT_FOUND;
		}
		return ResponseEntity.status(responseStatus)
				.contentType(MediaType.APPLICATION_JSON)
				.body(json);
	}

	@PostMapping("/employees/{id}")
	public ResponseEntity<String> createOrUpdateEmployeeById(@PathVariable String id){
		ResponseEntity<String> response;
		//if(!reportService.updateReportById(id).isEmpty())
			response = ResponseEntity.status(HttpStatus.OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body("Success");
		/*else
			response = ResponseEntity.status(HttpStatus.NOT_FOUND)
					.contentType(MediaType.APPLICATION_JSON)
					.body("Report is not found");*/
		return response;
	}

}
