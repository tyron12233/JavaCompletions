package com.tyron.javacompletion.completion;

import org.junit.jupiter.api.Test;

import static com.google.common.truth.Truth.assertThat;

public class TypeCast extends CompletorTest {

    @Test
    public void completeWithTypeCast() throws Exception {
        assertThat(
                getCandidateNames(
                        completeWithContent(
                                "CompleteInMethod.java", "((BelowClass) above)./** @complete */")))
                .contains("belowField");
    }
}
