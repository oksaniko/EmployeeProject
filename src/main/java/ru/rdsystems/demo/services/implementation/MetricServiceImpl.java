package ru.rdsystems.demo.services.implementation;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.services.MetricService;

@Service
@RequiredArgsConstructor
public class MetricServiceImpl implements MetricService {

	private final MeterRegistry meterRegistry;

	@Override
	public void increment(String metricName) {
		meterRegistry.counter(metricName).increment();
	}

	@Override
	public void increment(Counter counter) {
		counter.increment();
	}

}
