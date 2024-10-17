package ru.rdsystems.demo.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

@FeignClient(value = "randomUserRemoteClient", url = "${remote.randomuser.url}")
public interface RandomUserClient {

	ResponseEntity<Object> getRandomInfo();

}
