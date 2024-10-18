package ru.rdsystems.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.model.entities.KafkaErrorMessageEntity;

import java.util.Optional;

@Repository
public interface KafkaErrorMessageRepository extends JpaRepository<KafkaErrorMessageEntity, String> {

	Optional<KafkaErrorMessageEntity> findByMessageAndTopicName(String message, String topic);

}
