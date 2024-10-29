package ru.rdsystems.demo.services;

import io.micrometer.core.instrument.Counter;

public interface MetricService {

	void increment(String metricName);
	void increment(Counter counter);

}
