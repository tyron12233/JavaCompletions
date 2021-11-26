package com.tyron.javacompletion.completion;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Import extends CompletorTest {

    @Test
    public void completeImport() throws Exception {
        String baseImportCompletion = "import com.tyron.javacompletion./** @complete */";
        List<String> cases =
                ImmutableList.of(
                        baseImportCompletion,
                        baseImportCompletion + "\nimport java.util.List;",
                        "import java.util.List;\n" + baseImportCompletion);
        assertCompletion("CompleteOutOfClass.java", cases, "completion");
    }
}
