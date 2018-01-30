package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;

import java.util.Random;

public class MainController {


    public TableView tableView;
    public TextField titleField;
    public TextField descField;
    public TextField estField;
    public ChoiceBox ownerField;
    public Button addBtn;
    public ObservableList<Task> taskObservableList;
    public void initialize(){
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ownerField.setItems(FXCollections.observableArrayList(
                "lozog", "root", "sample", "kamil") //to add users dynamically from db.
        );
    }


    public void addButton(ActionEvent actionEvent) {
        int taskId = new Random().nextInt();
        Task task = new Task(taskId,titleField.getText(),  Integer.parseInt(estField.getText()), new User(ownerField.selectionModelProperty().toString()), "Task "+taskId+" of "+ownerField.selectionModelProperty().toString());
        taskObservableList.add(1, task);

    }
}
