package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Controller {
    public PasswordField passwordField;
    public TextField loginField;
    public Text actionTarget;
    public static boolean validateUser(User u){
        if (u.getUser_login().equals("root") && u.getUser_password().equals("admin")){
            return true;
        }else{
            return false;
        }
    }
    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        if(loginField.getText().equals("") || passwordField.equals("")){
            actionTarget.setText("puste");
        }else{
           if(validateUser(new User(loginField.getText(),passwordField.getText())))
                //display success and switch screen
               System.out.println("Success");
            else{
                actionTarget.setText("Wrong login data");
           }
        }
    }
}
