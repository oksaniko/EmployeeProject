package ru.rdsystems.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rdsystems.demo.model.KafkaErrorMessageEntity;

import java.util.Optional;

public interface KafkaErrorMessageRepository extends JpaRepository<KafkaErrorMessageEntity, String> {

	Optional<KafkaErrorMessageEntity> findByMessageAndTopicName(String message, String topic);

}
