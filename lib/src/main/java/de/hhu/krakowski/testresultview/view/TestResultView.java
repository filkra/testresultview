package de.hhu.krakowski.testresultview.view;

import de.hhu.krakowski.testresultview.adapter.TestResultAdapter;
import de.hhu.krakowski.testresultview.data.TestClass;
import de.hhu.krakowski.testresultview.data.TestResult;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.adapter.ReadOnlyJavaBeanObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.event.EventHandler;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class TestResultView extends TreeView<TestResult> {

    /**
     * A single entries row height.
     */
    public static final double ROW_HEIGHT = 20.0;

    /**
     * Stores all shown {@link TestResult}s.
     */
    private final ObservableSet<TestResult> mTestResults = FXCollections.observableSet();

    /**
     * Stores all shown class names.
     */
    private final Set<String> mClassNames = new HashSet<>();

    /**
     * Dummy item used as the root.
     */
    private final TreeItem<TestResult> mDummy = new TreeItem<>(TestResult.empty());

    /**
     * The currently selected test result.
     */
    private final ReadOnlyObjectWrapper<TestResult> mSelectedResult = new ReadOnlyObjectWrapper<>();

    /**
     * Creates an empty TreeView.
     *
     * <p>Refer to the {@link TreeView} class documentation for details on the
     * default state of other properties.</p>
     */
    public TestResultView() {
        init();
    }

    /**
     * Creates a TreeView with the provided root node.
     *
     * <p>Refer to the {@link TreeView} class documentation for details on the
     * default state of other properties.</p>
     *
     * @param root The node to be the root in this TreeView.
     */
    public TestResultView(TreeItem<TestResult> root) {
        super(root);
        init();
    }

    /**
     * Adds the specified {@link TestResult} to this view.
     *
     * @param testResult The {@link TestResult} to add.
     */
    public void add(TestResult testResult) {
        mTestResults.add(testResult);
    }

    /**
     * Removes the specified {@link TestResult} from this view.
     *
     * @param testResult The {@link TestResult} to remove.
     */
    public void remove(TestResult testResult) {
        mTestResults.remove(testResult);
    }

    /**
     * Adds a custom item to the list using the specified {@link TestResultAdapter}.
     *
     * @param element The custom item to add.
     * @param adapter The {@link TestResultAdapter} to use.
     * @param <T> The custom items type.
     */
    public <T> void add(T element, TestResultAdapter<T> adapter) {
        add(adapter.convert(element));
    }

    /**
     * Removes a custom item from the list using the specified {@link TestResultAdapter}.
     *
     * @param element The custom item to remove.
     * @param adapter The {@link TestResultAdapter} to use.
     * @param <T> The custom items type.
     */
    public <T> void remove(T element, TestResultAdapter<T> adapter) {
        remove(adapter.convert(element));
    }

    /**
     * Initializes this {@link TestResultView}.
     */
    private void init() {

        String css = getClass().getResource("/css/tree.css").toExternalForm();

        getStylesheets().add(css);

        setRoot(mDummy);

        setShowRoot(false);

        setFixedCellSize(ROW_HEIGHT);

        mTestResults.addListener(mChangeListener);

        getSelectionModel().selectedItemProperty().addListener(mSelectionListener);
    }

    /**
     * Finds the first {@link TreeItem} belonging to the given class name.
     *
     * @param className The class name to find.
     * @return The found {@link TreeItem} or {@code null} if none is found.
     */
    @Nullable
    private TreeItem<TestResult> findClassRoot(String className) {

        ObservableList<TreeItem<TestResult>> children = getRoot().getChildren();

        for (TreeItem<TestResult> result : children) {

            if (result.getValue().getClassName().equals(className)) {

                return result;
            }
        }

        return null;
    }

    /**
     * Adds a new {@link TreeItem} representing the given class name if it does not exist yet.
     *
     * @param className The class name.
     */
    private void addClassRoot(String className) {

        if (mClassNames.contains(className)) {
            return;
        }

        mClassNames.add(className);

        ObservableList<TreeItem<TestResult>> children = getRoot().getChildren();

        TestResult entry = new TestClass(className);

        TreeItem<TestResult> classRoot = new TreeItem<>(entry);

        classRoot.setGraphic(new TestResultRootCell(entry.getClassName()));

        classRoot.setExpanded(true);

        children.add(classRoot);
    }

    /**
     * Gets called after an item has been added to this {@link TestResultView}.
     *
     * @param item The added item.
     */
    private void onItemAdded(TestResult item) {

        String className = item.getClassName();

        addClassRoot(className);

        TreeItem<TestResult> tmp = findClassRoot(className);

        TreeItem<TestResult> element = new TreeItem<>(item);

        if (tmp == null) {
            return;
        }

        TestResultCell cell = new TestResultCell(item);

        element.setGraphic(cell);

        element.setExpanded(true);

        tmp.getChildren().add(element);
    }

    /**
     * Gets called after an item has been removed from this {@link TestResultView}.
     *
     * @param item The removed item.
     */
    private void onItemRemoved(TestResult item) {

        String className = item.getClassName();

        TreeItem<TestResult> classRoot = findClassRoot(className);

        if (classRoot == null) {
            return;
        }

        ObservableList<TreeItem<TestResult>> children = classRoot.getChildren();

        Iterator<TreeItem<TestResult>> iterator = children.iterator();

        TreeItem<TestResult> tmp;

        while (iterator.hasNext()) {

            tmp = iterator.next();

            if (tmp.getValue().getClassName().equals(className)) {

                iterator.remove();

                break;
            }
        }

        if (children.isEmpty()) {

            mClassNames.remove(className);

            mDummy.getChildren().remove(classRoot);
        }

    }

    /**
     * Listens for changes occuring on the {@link ObservableSet} backing this {@link TestResultView}.
     */
    private SetChangeListener<TestResult> mChangeListener = change -> {

        if (change.wasRemoved()) {

            onItemRemoved(change.getElementRemoved());
        }

        if (change.wasAdded()) {

            onItemAdded(change.getElementAdded());
        }
    };

    private ChangeListener<TreeItem<TestResult>> mSelectionListener = (observable, oldValue, newValue) -> {

        if (newValue.getParent() != mDummy) {
            mSelectedResult.set(newValue.getValue());
            return;
        }

        String description = newValue.getChildren().stream()
                .map(item -> item.getValue().getDescription())
                .filter(item -> !item.isEmpty())
                .collect(Collectors.joining("\n\n"));

        TestResult result = newValue.getValue();

        TestResult classRoot = new TestResult(result.getClassName(), "", description, result.getDuration(), result.isFailed());

        mSelectedResult.set(classRoot);
    };

    @Override
    public void requestFocus() {
        // Prevent focus
    }

    public ReadOnlyObjectProperty<TestResult> selectedResultProperty() {
        return mSelectedResult.getReadOnlyProperty();
    }

    public TestResult getSelectedResult() {
        return mSelectedResult.get();
    }
}
