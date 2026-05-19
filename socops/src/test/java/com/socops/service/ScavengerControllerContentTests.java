package com.socops.service;

import com.socops.data.IcebreakerPrompts;
import com.socops.web.BingoRestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller-level content tests for the scavenger endpoint (TDD-red).
 * These tests assert that the endpoint returns 24 unique prompts drawn
 * from the IcebreakerPrompts catalogue and that none are flagged as freeCell.
 */
@WebMvcTest(BingoRestController.class)
class ScavengerControllerContentTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("/api/bingo/scavenger returns 24 unique prompts from catalogue")
    void scavengerReturnsUniquePromptsFromCatalogue() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/bingo/scavenger"))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString();
        List<Map<String, Object>> items = objectMapper.readValue(body, new TypeReference<>() {});

        assertEquals(24, items.size(), "Scavenger endpoint must return 24 items");

        // none should be freeCell
        for (Map<String, Object> it : items) {
            Object free = it.get("freeCell");
            assertNotNull(free, "Each item should include a freeCell property");
            assertFalse(Boolean.parseBoolean(String.valueOf(free)), "Scavenger items must not be free cells");
            assertTrue(it.containsKey("prompt"), "Each item must include a prompt property");
        }

        Set<String> prompts = items.stream()
                .map(m -> String.valueOf(m.get("prompt")))
                .collect(Collectors.toSet());

        assertEquals(24, prompts.size(), "Prompts must be unique");
        assertTrue(IcebreakerPrompts.ALL_PROMPTS.containsAll(prompts), "All prompts must be drawn from the master catalogue");
    }
}
