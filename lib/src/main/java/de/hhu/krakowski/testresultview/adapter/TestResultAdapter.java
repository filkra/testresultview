package de.hhu.krakowski.testresultview.adapter;

import de.hhu.krakowski.testresultview.data.TestResult;

public interface TestResultAdapter<T> {

    TestResult convert(T element);
}
