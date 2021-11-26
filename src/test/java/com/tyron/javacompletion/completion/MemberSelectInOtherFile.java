package com.tyron.javacompletion.completion;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class MemberSelectInOtherFile extends CompletorTest {


    @Test
    public void completeMemberSelectionInOtherFile() throws Exception {
        List<CompletionCandidate> candidates =
                completeWithContent(
                        "CompleteInMethod.java",
                        "new OtherClass().innerClass./** @complete */",
                        "OtherClass.java");
        assertThat(getCandidateNames(candidates))
                .containsExactly("innerInnerClass", "getInnerInnerClass", "toString");
    }
}
