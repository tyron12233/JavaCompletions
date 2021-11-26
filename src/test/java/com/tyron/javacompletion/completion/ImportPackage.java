package com.tyron.javacompletion.completion;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ImportPackage extends CompletorTest {

    @Test
    public void completeImportPackage() throws Exception {
        List<String> cases =
                ImmutableList.of(
                        "import com.tyron.javacompletion.completion./** @complete */;",
                        "import static com.tyron.javacompletion.completion./** @complete */;");
        assertCompletion("CompleteOutOfClass.java", cases, "testdata");
    }
}
