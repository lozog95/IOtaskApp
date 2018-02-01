package sample;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Random;

public class MainController {
    public TableView<Task> tableView;
    public TextField titleField;
    public TextField estField;
    public ChoiceBox ownerField;
    public Button addBtn;
    public ObservableList<Task> taskObservableList;
    @FXML
    Button upBtn = new Button("Up");
    @FXML
    Button downBtn = new Button ("Down");

    public void initialize(){

        taskObservableList=FXCollections.observableArrayList();
        ownerField.setItems(FXCollections.observableArrayList(
                "lozog", "root", "sample", "kamil") //to add users dynamically from db.

        );

        TableColumn title = new TableColumn("Task");

        title.setCellValueFactory(
                new PropertyValueFactory<>("description"));


        tableView.setItems(taskObservableList);
        tableView.getColumns().add(title);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ReadOnlyIntegerProperty selectedIndex = tableView.getSelectionModel().selectedIndexProperty();
        downBtn.disableProperty().bind(selectedIndex.isEqualTo((tableView.getItems().size()-1)));
        upBtn.disableProperty().bind(selectedIndex.lessThanOrEqualTo(0));
    }

    public void addButton(ActionEvent actionEvent) {
        int taskId = new Random().nextInt();
        String userLogin=ownerField.getValue().toString();
        User user= new User(userLogin);
        System.out.println(userLogin);
        Task task=new Task(taskId,titleField.getText(),Integer.parseInt(estField.getText()), user, "["+ 1+"] "+titleField.getText()+" | Assignee: "+ user.getUser_login()+" | Estimate: "+estField.getText());
        taskObservableList.add(task);
        System.out.println(task.getDescription());
        tableView.refresh();
    }



    public void upBtn(ActionEvent actionEvent){
        int index = tableView.getSelectionModel().getSelectedIndex();
        tableView.getItems().add(index-1, tableView.getItems().remove(index));
        tableView.getSelectionModel().clearAndSelect(index-1);
    }

    public void downBtn(ActionEvent actionEvent) {
        int index = tableView.getSelectionModel().getSelectedIndex();
        tableView.getItems().add(index+1, tableView.getItems().remove(index));
        tableView.getSelectionModel().clearAndSelect(index+1);
        System.out.println(tableView.getItems().size());
    }
}
