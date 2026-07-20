package com.alchemain.rx.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.alchemain.rx.bus.JsonFiles;
import com.fasterxml.jackson.databind.JsonNode;

public class JsonFilesIntegrationTest {

	@Test
	public void readsPersonJsonFromClasspath() throws Exception {
		JsonNode person = JsonFiles.readTree("person.json");

		assertNotNull(person);
		assertEquals("Irene", person.get("givenName").asText());
		assertEquals("Rodriguez", person.get("familyName").asText());
		assertTrue(person.get("phoneNumbers").size() > 0);
	}
}