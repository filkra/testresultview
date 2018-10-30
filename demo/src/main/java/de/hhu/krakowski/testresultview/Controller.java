package de.hhu.krakowski.testresultview;

import de.hhu.krakowski.testresultview.adapter.TestResultAdapter;
import de.hhu.krakowski.testresultview.data.TestResult;
import de.hhu.krakowski.testresultview.view.TestResultView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TestResultView mTestResultView;

    @FXML
    private TextArea mDescriptionArea;

    private IllegalArgumentException mNotHot = new IllegalArgumentException("Man can never be hot, perspiration ting.");

    private ArithmeticException mMath = new ArithmeticException("Two plus two is four, minus one that's three, quick maths.");

    private Random mRandom = new Random();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mTestResultView.add(new TestResult("BigShakTest", "testIsHot", mNotHot.toString(), mRandom.nextInt(300), true));

        mTestResultView.add(new TestResult("BigShakTest", "testIsNotHot","", mRandom.nextInt(300), false));

        mTestResultView.add("BigShakTest::testMath::" + mMath.toString(), element -> {

            String[] info = element.split("::");

            return new TestResult(info[0], info[1], info[2], mRandom.nextInt(300), true);
        });

        mTestResultView.selectedResultProperty().addListener((observable, oldValue, newValue) -> {

            mDescriptionArea.setText(newValue.getDescription());
        });
    }
}
