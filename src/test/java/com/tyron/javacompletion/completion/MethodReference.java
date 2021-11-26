package com.tyron.javacompletion.completion;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

public class MethodReference extends CompletorTest {

    @Test
    public void completeMethodReference() throws Exception {
        assertCompletion(
                "CompleteInMethod.java",
                ImmutableList.of("CompleteInMethod::/** @complete */"),
                "staticMethod");
        assertCompletion(
                "CompleteInMethod.java",
                ImmutableList.of("this::/** @complete */"),
                "completeMethod",
                "toString");
    }

}
