package com.tyron.javacompletion.completion.testdata;

public class CompleteInMethod {
    public static final int STATIC_FIELD = 0;
    public CompleteInMethod self = new CompleteInMethod();
    public FakeString fakeString;

    /** The class above. */
    public static class AboveClass {
        public static final int STATIC_ABOVE_FIELD = 0;
        public final int aboveField = 0;

        public static void staticAboveMethod() {}
        public void aboveMethod() {}
    }

    public void completeMethod() {
        AboveClass above = new AboveClass();
        BelowClass below = new BelowClass();
        /** @insert */
    }

    public static void staticMethod() {

    }
    public static void staticMethod(int value) {

    }

    /** The class below. */
    public static class BelowClass {
        public static final int STATIC_BELOW_FIELD = 0;
        public final int belowField = 0;

        public static void staticBelowMethod() {}
        public void belowMethod() {}
    }
}