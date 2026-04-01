package org.security_diaries;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.bdd_manager.UserManager;

import java.io.IOException;

public class HelloController {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @FXML
    private void connect(){
        String s_username = this.username.getText();
        String s_password = this.password.getText();
        boolean connected = UserManager.connexion(s_username, s_password);

        if (connected){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/diaries-view.fxml"));
                Parent diaries = loader.load();

                Stage stage = (Stage) username.getScene().getWindow();

                stage.setScene(new Scene(diaries));
                stage.setTitle("Journal de sécurité");
                stage.show();

            } catch (IOException e){
                e.printStackTrace();
            }
        }



    }





}
