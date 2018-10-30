package de.hhu.krakowski.testresultview.view;

import de.hhu.krakowski.testresultview.data.TestResult;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;

import static de.hhu.krakowski.testresultview.view.TestResultView.ROW_HEIGHT;

class TestResultCell extends HBox {

    private final TestResult mTestResult;

    private static final double ICON_RADIUS = 4;

    private static final Paint PAINT_FAIL = Paint.valueOf("#EF1401");

    private static final Paint PAINT_SUCCESS = Paint.valueOf("#1DB40F");

    /**
     * Creates a new {@link TestResultCell} using the specified {@link TestResult}.
     *
     * @param testResult The {@link TestResult} to use.
     */
    TestResultCell(TestResult testResult) {

        setFillHeight(true);

        setAlignment(Pos.CENTER_LEFT);

        setPrefHeight(ROW_HEIGHT);

        mTestResult = testResult;

        Label testName = new Label(testResult.getTestName());

        Label testDuration = new Label(String.valueOf(testResult.getDuration()) + " ms");

        Circle circle = getStatusIcon(testResult.isFailed());

        Region spacing = new Region();

        Region margin = new Region();

        margin.setPrefWidth(4.0);

        getChildren().addAll(circle, margin, testName, spacing, testDuration);

        setHgrow(spacing, Priority.ALWAYS);
    }

    /**
     * Returns the {@link TestResult} contained in this {@link TestResultCell}.
     *
     * @return The {@link TestResult} contained in this {@link TestResultCell}.
     */
    public TestResult getTestResult() {
        return mTestResult;
    }

    private static Circle getStatusIcon(boolean isFailed) {

        Paint paint = isFailed ? PAINT_FAIL : PAINT_SUCCESS;

        return new Circle(ICON_RADIUS, paint);
    }
}
