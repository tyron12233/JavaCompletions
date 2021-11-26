package com.tyron.javacompletion.completion;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class LambdaParameterType extends CompletorTest {

    @Test
    public void checkLambdaParameter() {
        List<CompletionCandidate> completionCandidates = completeWithContent("LambdaParameterType.java",
                "string./** @complete */",
                "FakeString.java");
        assertThat(getCandidateNames(completionCandidates))
                .containsExactly("fakeField", "fakeMethod", "toString");
    }
}
