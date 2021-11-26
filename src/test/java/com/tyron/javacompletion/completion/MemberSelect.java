package com.tyron.javacompletion.completion;

import com.google.common.collect.ImmutableList;
import com.tyron.javacompletion.completion.CompletorTest;
import org.junit.jupiter.api.Test;

import java.util.List;

public class MemberSelect extends CompletorTest {

    @Test
    public void completeMemberSelection() throws Exception {
        String baseAboveCompletion = "above./** @complete */";
        List<String> aboveCases =
                ImmutableList.of(baseAboveCompletion, baseAboveCompletion + "\nabove.aboveMethod();");
        assertCompletion("CompleteInMethod.java", aboveCases, "aboveField", "aboveMethod", "toString");

        String baseBelowCompletion = "below./** @complete */";
        List<String> belowCases =
                ImmutableList.of(
                        baseBelowCompletion,
                        baseBelowCompletion + "\nbelow.belowMethod();",
                        "above.;" + baseBelowCompletion,
                        "self.new BelowClass()./** @complete */");
        assertCompletion("CompleteInMethod.java", belowCases, "belowField", "belowMethod", "toString");
    }

    @Test
    public void completeArrayMemberSelection() throws Exception {
        assertCompletion("CompleteArray.java", "ARRAY./** @complete */", "length");
    }
}
