package ru.rdsystems.demo.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "kafka_errors")
public class KafkaErrorMessageEntity {

	@Id
	@Column(length = 32, nullable = false)
	private String id;

	@Column(nullable = false)
	private LocalDateTime errorCreateDate;

	@Column(nullable = false)
	private LocalDateTime errorLastSentDate;

	@Column(nullable = false)
	private String topicName;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private String errorTxt;
}
