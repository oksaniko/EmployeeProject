package ru.rdsystems.demo.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(value = "randomUserRemoteClient", url = "${remote.randomuser.url}")
public interface RandomUserClient {

	@GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<Map<String, Object>> getRandomInfo();

}
