package ru.rdsystems.demo.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.rdsystems.demo.model.enums.Position;
import ru.rdsystems.demo.model.enums.Status;

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
	private Position position;

	@Column(length = 20, nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "random_data", columnDefinition = "jsonb")
	private String randomData;

}
