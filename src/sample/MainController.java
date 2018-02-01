package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class MainController {
    private static final char DEFAULT_SEPARATOR = ',';
    private static final char DEFAULT_QUOTE = '"';
    public final String usersCsv="src/users.csv";
    public static ArrayList<User> usersList;
    public TableView<Task> tableView;
    public TextField titleField;
    public TextField estField;
    public ChoiceBox ownerField;
    public Button addBtn;
    public ObservableList<Task> taskObservableList;
    public void initialize(){
        taskObservableList=FXCollections.observableArrayList();

        try {
            usersList=loadUsersCSV(usersCsv);
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


        tableView.setItems(taskObservableList);
        tableView.getColumns().add(title);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

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

}
