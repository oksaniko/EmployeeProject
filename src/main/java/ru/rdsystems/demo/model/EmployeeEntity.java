package ru.rdsystems.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "employees")
public class EmployeeEntity {

	@Id
	@Column(length = 32, nullable = false)
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private EmployeePosition position;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private EmployeeStatus status;

	public enum EmployeePosition{
		MANAGER, EMPLOYEE, UNDEFINED, TECH
	}

	public enum EmployeeStatus {
		WORKING, TRIAL, TIME_OFF, DISMISSED, DELETED
	}

}
