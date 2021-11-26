package com.tyron.javacompletion.completion;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class ImportInnerClass extends CompletorTest {

    @Test
    public void completeImportInnerClass() throws Exception {
        String toComplete = "import com.tyron.javacompletion.completion.testdata.OtherClass./** @complete */";
        List<CompletionCandidate> candidates =
                completeWithContent("CompleteOutOfClass.java", toComplete, "OtherClass.java");
        assertThat(getCandidateNames(candidates)).containsExactly("InnerClass");
    }
}
