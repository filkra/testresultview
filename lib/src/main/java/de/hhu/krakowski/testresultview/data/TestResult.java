package de.hhu.krakowski.testresultview.data;

public class TestResult {

    private final String mClassName;

    private final String mTestName;

    private final String mDescription;

    private final long mDuration;

    private final boolean mFailed;

    public TestResult(String className, String testName, String description, long duration, boolean failed) {
        mClassName = className;
        mTestName = testName;
        mDescription = description;
        mDuration = duration;
        mFailed = failed;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getTestName() {
        return mTestName;
    }

    public String getDescription() {
        return mDescription;
    }

    public long getDuration() {
        return mDuration;
    }

    public boolean isFailed() {
        return mFailed;
    }

    public static TestResult empty() {
        return new TestResult("", "", "", 0, false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestResult that = (TestResult) o;

        if (!mClassName.equals(that.mClassName)) return false;
        return mTestName.equals(that.mTestName);
    }

    @Override
    public int hashCode() {
        int result = mClassName.hashCode();
        result = 31 * result + mTestName.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "";
    }
}
