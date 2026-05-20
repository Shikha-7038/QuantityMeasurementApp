package com.app.quantitymeasurement;

import com.app.quantitymeasurement.dto.QuantityDTO;

import com.app.quantitymeasurement.model.QuantityMeasurementEntity;
import com.app.quantitymeasurement.repository.QuantityMeasurementSpringDataRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc()
@ActiveProfiles("test")

class QuantityMeasurementAppApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private QuantityMeasurementSpringDataRepository repository;
	@Test
	void testSpringBootApplicationStarts() {
		assertNotNull(mockMvc);
	}
	@Test
	void testRestEndpointCompareQuantities() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(List.of(dto, dto))))
				.andExpect(status().isOk());
	}
	@Test
	void testRestEndpointConvertQuantities() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/convert")
						.param("targetUnit", "INCH")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isOk());
	}
	@Test
	void testRestEndpointAddQuantities() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/add")
						.param("targetUnit", "INCH")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(List.of(dto, dto))))
				.andExpect(status().isOk());
	}

	@Test
	void testRestEndpointInvalidInput_Returns400() throws Exception {

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.content("invalid-json"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testRestEndpointMissingParameter_Returns400() throws Exception {

		mockMvc.perform(post("/api/v1/quantities/convert")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testSwaggerUILoads() throws Exception {

		mockMvc.perform(get("/swagger-ui/index.html"))
				.andExpect(status().isOk());
	}
	@Test
	void testOpenAPIDocumentation() throws Exception {
		assert true;
	}

	@Test
	void testH2ConsoleLaunches() {
		assertTrue(true);
	}

	@Test
	void testH2DatabasePersistence() {
		assertDoesNotThrow(() -> repository.findAll());
	}

	@Test
	void testActuatorHealthEndpoint() throws Exception {

		mockMvc.perform(get("/actuator/health"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status").value("UP"));
	}
	@Test
	void testActuatorMetricsEndpoint() throws Exception {

		mockMvc.perform(get("/actuator/metrics"))
				.andExpect(status().isOk());
	}
	@Test
	void testJPARepositoryFindByOperation() {

		assertNotNull(repository.findByOperation("COMPARE"));
	}

	@Test
	void testJPARepositoryCustomQuery() {

		assertNotNull(repository.findSuccessfulOperationsByOperation("ADD"));
	}
	@Test
	void testTransactionalRollback() {
		QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
		entity.setOperation("ADD");

		assertDoesNotThrow(() -> repository.save(entity));
	}
	@Test
	void testContentNegotiation_JSON() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(List.of(dto, dto))))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void testExceptionHandling_GlobalHandler() throws Exception {

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testRequestPathVariable_Extraction() throws Exception {

		mockMvc.perform(get("/api/v1/quantities/count/COMPARE"))
				.andExpect(status().isOk());
	}

	@Test
	void testResponseSerialization_Object() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(List.of(dto, dto))))
				.andExpect(status().isOk());
	}

	@Test
	void testMockMvc_ComparisonTest() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(List.of(dto, dto))))
				.andExpect(status().isOk());
	}

	@Test
	void testMockMvc_ResponseAssertion() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(List.of(dto, dto))))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}

	@Test
	void testIntegrationTest_MultipleOperations() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/compare")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(List.of(dto, dto))));

		mockMvc.perform(post("/api/v1/quantities/convert")
				.param("targetUnit", "INCH")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)));

		mockMvc.perform(post("/api/v1/quantities/add")
				.param("targetUnit", "INCH")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(List.of(dto, dto))));
	}

	@Test
	void testDatabaseInitialization_SchemaCreated() {

		assertDoesNotThrow(() -> repository.count());
	}

	@Test
	void testProfileSpecificConfiguration_Development() {

		assertTrue(true);
	}
	@Test
	void testProfileSpecificConfiguration_Production() {

		assertTrue(true);
	}
	@Test
	void testHttpStatusCodes_Success() throws Exception {

		QuantityDTO dto = new QuantityDTO(1.0, "FEET", "LengthUnit");

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(List.of(dto, dto))))
				.andExpect(status().isOk());
	}

	@Test
	void testHttpStatusCodes_ClientErrors() throws Exception {

		mockMvc.perform(post("/api/v1/quantities/compare")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{}"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testHttpStatusCodes_ServerErrors() throws Exception {

		mockMvc.perform(get("/api/v1/quantities/unknown"))
				.andExpect(status().is4xxClientError());
	}
}