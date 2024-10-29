package ru.rdsystems.demo;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Configurator {

	private final MeterRegistry meterRegistry;

	@Value("${remote.randomuser.metrics.count.name}")
	private String counterMetric;

	@Bean
	public Counter createCounterMetric(){
		return Counter
				.builder(counterMetric)
				.register(meterRegistry);
	}

}
