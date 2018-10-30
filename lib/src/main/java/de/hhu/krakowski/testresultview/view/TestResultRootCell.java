package de.hhu.krakowski.testresultview.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

class TestResultRootCell extends HBox {

    TestResultRootCell(String className) {

        setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(className);

        getChildren().add(label);
    }

}
