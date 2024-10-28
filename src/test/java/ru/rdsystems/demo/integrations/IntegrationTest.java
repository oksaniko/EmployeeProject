package ru.rdsystems.demo.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.rdsystems.demo.model.entities.EmployeeEntity;
import ru.rdsystems.demo.repositories.EmployeeRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class IntegrationTest {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		//dataProvider.deleteEmployees();
		repository.deleteAll();
	}

	@AfterEach
	void tearDown() {
	}

	private static Stream<Arguments> dataProvider(){
		return Stream.of(
				Arguments.of(Instancio.create(EmployeeEntity.class)),
				Arguments.of(Instancio.create(EmployeeEntity.class)),
				Arguments.of(Instancio.create(EmployeeEntity.class))
		);
	}

	@ParameterizedTest
	@MethodSource("dataProvider")
	void getEmployees(EmployeeEntity employee) throws Exception {
		//dataProvider.save(employee);
		log.info("instancio: {}",new ObjectMapper().writeValueAsString(employee));
		employee.setRandomData(null);
		repository.save(employee);
		MockHttpServletResponse response = mockMvc
				.perform(MockMvcRequestBuilders.get("/employees"))
				.andDo(print())
				.andExpect(status().is2xxSuccessful())
				.andReturn()
				.getResponse();

		var actualResponse = new ObjectMapper().readValue(response.getContentAsString(),
				new TypeReference<Map<String, List<EmployeeEntity>>>() {});
		assertThat(actualResponse.get("employees")).usingRecursiveComparison().isEqualTo(List.of(employee));
	}

}
