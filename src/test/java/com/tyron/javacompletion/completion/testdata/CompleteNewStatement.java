package com.tyron.javacompletion.completion.testdata;

import java.util.List;

public class CompleteNewStatement {
    private static final String CONSTANT = "constant";
    private final int memberField = 1;

    public CompleteNewStatement() {}

    public void staticMethod() {}

    public class InnerClass extends CompleteNewStatement {
        private final String subClassMemberField = "";

        public void memberMethod(boolean param1, String stringParam) {
            /** @complete */
        }
    }
}