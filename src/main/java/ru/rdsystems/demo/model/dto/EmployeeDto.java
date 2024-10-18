package ru.rdsystems.demo.model.dto;

import lombok.Data;
import ru.rdsystems.demo.model.enums.Position;
import ru.rdsystems.demo.model.enums.Status;

@Data
public class EmployeeDto {

	private String name;
	private Position position;
	private Status status;

}
