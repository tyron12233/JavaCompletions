package com.tyron.javacompletion.completion;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class TwoDots extends CompletorTest {

    @Test
    public void completeTwoDots() throws Exception {
        String toComplete = "above./** @complete */.something";

        List<CompletionCandidate> candidates = completeWithContent("CompleteInMethod.java", toComplete);
        assertThat(getCandidateNames(candidates))
                .containsAtLeast("aboveField", "aboveMethod", "toString");
    }
}
