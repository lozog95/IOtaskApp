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
    @FXML
    Button upBtn = new Button("Up");
    @FXML
    Button downBtn = new Button ("Down");

    public void initialize(){

        taskObservableList=FXCollections.observableArrayList();

        try {
            usersList=loadUsersCSV(usersCsv);
            tasksList=loadTasksCSV(tasksCsv);

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
        writeLine(writer,taskList);
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

    public ArrayList<User> loadUsersCSV(String path) throws FileNotFoundException {
        ArrayList<User> usersList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine());
            System.out.println("user " + line.get(0));
            usersList.add(new User(line.get(0)));
        }
        scanner.close();
        return usersList;
    }

    public ArrayList<Task> loadTasksCSV(String path) throws FileNotFoundException {
        ArrayList<Task> tasksList = new ArrayList<>();
        Scanner scanner = new Scanner(new File(path));
        while (scanner.hasNext()) {
            List<String> line = parseLine(scanner.nextLine());
            System.out.println("task " + line.get(0));
            tasksList.add(new Task(Integer.parseInt(line.get(0)), line.get(1), Integer.parseInt(line.get(2)), line.get(3)));
        }
        scanner.close();
        return tasksList;
    }

    public static List<String> parseLine(String cvsLine) {
        return parseLine(cvsLine, DEFAULT_SEPARATOR, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators) {
        return parseLine(cvsLine, separators, DEFAULT_QUOTE);
    }

    public static List<String> parseLine(String cvsLine, char separators, char customQuote) {

        List<String> result = new ArrayList<>();

        //if empty, return!
        if (cvsLine == null && cvsLine.isEmpty()) {
            return result;
        }

        if (customQuote == ' ') {
            customQuote = DEFAULT_QUOTE;
        }

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuffer curVal = new StringBuffer();
        boolean inQuotes = false;
        boolean startCollectChar = false;
        boolean doubleQuotesInColumn = false;

        char[] chars = cvsLine.toCharArray();

        for (char ch : chars) {

            if (inQuotes) {
                startCollectChar = true;
                if (ch == customQuote) {
                    inQuotes = false;
                    doubleQuotesInColumn = false;
                } else {

                    //Fixed : allow "" in custom quote enclosed
                    if (ch == '\"') {
                        if (!doubleQuotesInColumn) {
                            curVal.append(ch);
                            doubleQuotesInColumn = true;
                        }
                    } else {
                        curVal.append(ch);
                    }

                }
            } else {
                if (ch == customQuote) {

                    inQuotes = true;

                    //Fixed : allow "" in empty quote enclosed
                    if (chars[0] != '"' && customQuote == '\"') {
                        curVal.append('"');
                    }

                    //double quotes in column will hit this!
                    if (startCollectChar) {
                        curVal.append('"');
                    }

                } else if (ch == separators) {

                    result.add(curVal.toString());

                    curVal = new StringBuffer();
                    startCollectChar = false;

                } else if (ch == '\r') {
                    //ignore LF characters
                    continue;
                } else if (ch == '\n') {
                    //the end, break!
                    break;
                } else {
                    curVal.append(ch);
                }
            }

        }

        result.add(curVal.toString());

        return result;
    }

    public static void writeLine(Writer w, List<String> values) throws IOException {
        writeLine(w, values, DEFAULT_SEPARATOR, ' ');
    }

    public static void writeLine(Writer w, List<String> values, char separators) throws IOException {
        writeLine(w, values, separators, ' ');
    }

    //https://tools.ietf.org/html/rfc4180
    private static String followCVSformat(String value) {

        String result = value;
        if (result.contains("\"")) {
            result = result.replace("\"", "\"\"");
        }
        return result;

    }

    public static void writeLine(Writer w, List<String> values, char separators, char customQuote) throws IOException {

        boolean first = true;

        //default customQuote is empty

        if (separators == ' ') {
            separators = DEFAULT_SEPARATOR;
        }

        StringBuilder sb = new StringBuilder();
        for (String value : values) {
            if (!first) {
                sb.append(separators);
            }
            if (customQuote == ' ') {
                sb.append(followCVSformat(value));
            } else {
                sb.append(customQuote).append(followCVSformat(value)).append(customQuote);
            }

            first = false;
        }
        sb.append("\n");
        w.append(sb.toString());
    }


}
