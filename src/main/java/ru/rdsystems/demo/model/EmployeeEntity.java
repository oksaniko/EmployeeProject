package ru.rdsystems.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "random_data", columnDefinition = "jsonb")
	private String randomData;

	public enum EmployeePosition{
		MANAGER, EMPLOYEE, UNDEFINED, TECH
	}

	public enum EmployeeStatus {
		WORKING, TRIAL, TIME_OFF, DISMISSED, DELETED
	}

}
