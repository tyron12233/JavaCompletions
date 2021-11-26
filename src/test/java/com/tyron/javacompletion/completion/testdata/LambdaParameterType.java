package com.tyron.javacompletion.completion.testdata;

import java.lang.FakeString;

public class LambdaParameterType {

    private interface Lambda {
        default void invokeDefault() {

        }
        static void test() {

        }
        void invoke(FakeString string, int i );
    }

    public static void test(Lambda lambda) {
        lambda.invoke(null, 0);
    }
    public static void main() {
        test((string, i) -> {
            /** @insert */
        });
    }
}
