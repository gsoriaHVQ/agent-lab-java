package com.socops.service;

import com.socops.model.BingoCell;
import com.socops.data.IcebreakerPrompts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests (TDD-red) that assert the presence of a BoardAssembler
 * API convenient for the Scavenger Hunt feature.
 *
 * These tests are intentionally written before implementation: they
 * should fail until BoardAssembler exposes assembleScavengerList().
 */
class BoardAssemblerUnitScavengerTests {

    @Test
    @DisplayName("assembleScavengerList returns 24 non-free prompts")
    void assembleScavengerListReturns24NonFreePrompts() {
        List<BingoCell> scav = BoardAssembler.assembleScavengerList();
        assertNotNull(scav, "Scavenger list must not be null");
        assertEquals(24, scav.size(), "Scavenger list must contain exactly 24 items (centre excluded)");

        for (BingoCell c : scav) {
            assertFalse(c.freeCell(), "Scavenger items must not include the free centre cell");
            assertNotNull(c.prompt(), "Each scavenger item must have a prompt");
            assertFalse(c.prompt().isBlank(), "Prompt text must not be blank");
        }
    }

    @Test
    @DisplayName("Scavenger prompts are unique and drawn from catalogue")
    void scavengerPromptsUniqueAndFromCatalogue() {
        List<BingoCell> scav = BoardAssembler.assembleScavengerList();
        Set<String> prompts = scav.stream().map(BingoCell::prompt).collect(Collectors.toSet());

        assertEquals(24, prompts.size(), "Prompts in scavenger list must be unique (24 distinct prompts)");
        assertTrue(IcebreakerPrompts.ALL_PROMPTS.containsAll(prompts), "All scavenger prompts should be drawn from the master catalogue");
    }
}
