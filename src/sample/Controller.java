package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class Controller {
    public PasswordField passwordField;
    public TextField loginField;
    public Text actionTarget;

    public void handleSubmitButtonAction(ActionEvent actionEvent) {
        if(loginField.getText().equals("") || passwordField.equals("")){
            actionTarget.setText("puste");
        }else{
            actionTarget.setText("nie puste!");
        }
    }
}
