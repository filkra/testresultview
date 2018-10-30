package de.hhu.krakowski.testresultview.data;

public class TestClass extends TestResult {

    private static final String TEST_NAME = "";

    private static final String TEST_DESCRIPTION = "";

    private static final long TEST_DURATION = 0;

    private static final boolean TEST_FAILED = false;

    public TestClass(String className) {
        super(className, TEST_NAME, TEST_DESCRIPTION, TEST_DURATION, TEST_FAILED);
    }

}
