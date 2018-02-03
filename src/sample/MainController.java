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

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;



public class MainController {
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    public final String usersCsv="src/users.csv";
    public final String tasksCsv="src/tasks.csv";
    public static ArrayList<User> usersList;
    public static ArrayList<Task> tasksList;
    public TableView<Task> tableView;
    public TextField titleField;
    public TextField estField;
    public ChoiceBox ownerField;
    public Button addBtn;
    public ObservableList<Task> taskObservableList;
    CSVUtils csvObject = new CSVUtils();
    @FXML
    Button upBtn = new Button("Up");
    @FXML
    Button downBtn = new Button ("Down");


    public void initialize(){

        taskObservableList=FXCollections.observableArrayList();
        
        

        try {
            usersList=csvObject.loadUsersCSV(usersCsv);
            tasksList=csvObject.loadTasksCSV(tasksCsv);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("User w pierwszej linii "+ usersList.get(1).getUser_login());
        ArrayList<String> loginsList =new ArrayList<>();
        for (User u: usersList){
            loginsList.add(u.getUser_login());
        }

        ownerField.setItems(FXCollections.observableArrayList(loginsList));
        TableColumn title = new TableColumn("Task");
        title.setCellValueFactory(
                new PropertyValueFactory<>("description"));

        taskObservableList.setAll(tasksList);
        tableView.setItems(taskObservableList);
        tableView.getColumns().add(title);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getSelectionModel().select(0);
        ReadOnlyIntegerProperty selectedIndex = tableView.getSelectionModel().selectedIndexProperty();
        downBtn.disableProperty().bind(selectedIndex.isEqualTo((tableView.getItems().size()-1)));
        upBtn.disableProperty().bind(selectedIndex.lessThanOrEqualTo(0));
    }

    public void addButton(ActionEvent actionEvent) throws IOException {
        int taskId = tableView.getItems().size()+1;
        String userLogin=ownerField.getValue().toString();
        System.out.println(userLogin);
        Task task=new Task(taskId,titleField.getText(),Integer.parseInt(estField.getText()), userLogin, "["+ taskId+"] "+titleField.getText()+" | Assignee: "+ userLogin+" | Estimate: "+estField.getText());
        taskObservableList.add(task);
        System.out.println(task.getDescription());
        tableView.refresh();
        FileWriter writer = new FileWriter(tasksCsv, true);
        List<String> taskList = new ArrayList<>();
        taskList.add(String.valueOf(task.getId()));
        taskList.add(task.getTitle());
        taskList.add(String.valueOf(task.getEstimate()));
        taskList.add(task.getOwner());
        csvObject.writeLine(writer,taskList);
        writer.flush();
        writer.close();

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
