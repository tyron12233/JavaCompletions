package com.tyron.javacompletion.completion;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

public class NewStatement extends CompletorTest {

    @Test
    public void completeNewStatement() throws Exception {
        List<String> keywords =
                Arrays.stream(KeywordCompletionCandidate.values())
                        .map(KeywordCompletionCandidate::getName)
                        .collect(Collectors.toList());
        List<String> expectedMembers =
                ImmutableList.of(
                        "CompleteNewStatement",
                        "param1",
                        "stringParam",
                        "CONSTANT",
                        "InnerClass",
                        "List",
                        "subClassMemberField",
                        "memberField",
                        "memberMethod",
                        "staticMethod",
                        "staticMethod", // TODO: Fix duplicate.
                        "com",
                        // From java.lang
                        "java",
                        "Object",
                        // From Object
                        "toString",
                        "toString");
        assertThat(getCandidateNames(completeTestFile("CompleteNewStatement.java")))
                .containsExactlyElementsIn(Iterables.concat(expectedMembers, keywords));
    }
}
