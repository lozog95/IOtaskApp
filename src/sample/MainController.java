package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Random;

public class MainController {


    public TableView<Task> tableView;
    public TextField titleField;
    public TextField descField;
    public TextField estField;
    public ChoiceBox ownerField;
    public Button addBtn;
    public ObservableList<Task> taskObservableList;
    public void initialize(){
        taskObservableList=FXCollections.observableArrayList();
        ownerField.setItems(FXCollections.observableArrayList(
                "lozog", "root", "sample", "kamil") //to add users dynamically from db.
        );

        Task task = new Task(1, "task", 3, new User("lozog"), "ddd");
        taskObservableList.add(task);
        TableColumn title = new TableColumn("Task");

        title.setCellValueFactory(
                new PropertyValueFactory<>("description"));


        tableView.setItems(taskObservableList);
        tableView.getColumns().add(title);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }


    public void addButton(ActionEvent actionEvent) {
        int taskId = new Random().nextInt();
       // User user = new User(taskId, titleField.getText(), estField.getText(),  );
        Task task = new Task(taskId,titleField.getText(),  Integer.parseInt(estField.getText()), new User(ownerField.selectionModelProperty().toString()), "Task "+taskId+" of "+ownerField.selectionModelProperty().toString());
        taskObservableList.add(task);
        System.out.println(task.getDescription());
        tableView.refresh();
    }
}
