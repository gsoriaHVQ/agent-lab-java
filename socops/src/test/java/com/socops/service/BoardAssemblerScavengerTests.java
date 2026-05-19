package com.socops.service;

import com.socops.web.BingoRestController;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.socops.data.IcebreakerPrompts;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.everyItem;

/**
 * Integration-style controller tests written in TDD red: these assert the
 * presence of a dedicated scavenger endpoint and will fail until implemented.
 */
@WebMvcTest(BingoRestController.class)
class BoardAssemblerScavengerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/bingo/fresh-board returns 25 items")
    void freshBoardReturns25() throws Exception {
        mockMvc.perform(get("/api/bingo/fresh-board"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(25));
    }

    @Test
    @DisplayName("GET /api/bingo/fresh-board includes free centre cell at index 12")
    void freshBoardHasFreeCentreCell() throws Exception {
        mockMvc.perform(get("/api/bingo/fresh-board"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[12].freeCell", is(true)))
                .andExpect(jsonPath("$[12].prompt", is(IcebreakerPrompts.FREE_CELL_LABEL)));
    }

    @Test
    @DisplayName("GET /api/bingo/scavenger returns 24 items (centre excluded)")
    void scavengerEndpointReturns24() throws Exception {
        mockMvc.perform(get("/api/bingo/scavenger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(24));
    }

    @Test
    @DisplayName("GET /api/bingo/scavenger items have no freeCell and include expected fields")
    void scavengerItemsStructure() throws Exception {
        mockMvc.perform(get("/api/bingo/scavenger"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(24))
                .andExpect(jsonPath("$[*].freeCell").value(everyItem(is(false))))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].prompt").exists());
    }
}
