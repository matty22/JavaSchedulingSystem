
package view_controller;

import dao.UserDao;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.User;
import utilities.Utils;


public class LoginViewController implements Initializable {

    // FXML variables for view controls
    @FXML private Button exitButton;
    @FXML private Button submitButton;
    @FXML private TextField usernameField;
    @FXML private TextField passwordField;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Get user geolocation
        Locale currentLocale = Locale.getDefault();
        String language = currentLocale.getDisplayLanguage();
    }    
    
    
    public void loginSubmitButtonHandler(ActionEvent event) throws IOException, SQLException {
        // Query dB for user entered into username field
        User user = UserDao.getUser(usernameField.getText());
        
        // If no such user, throw error alert
        if(user == null) {
            Utils.throwErrorAlert("Your username or password is invalid.");
        } else {
            // Get that user's real password
            String officialPassword = user.getUserPassword();
        
            // Get what they entered into the password field
            String enteredPass = passwordField.getText();
        
            try {
                // If entered password, matches password from dB, change scene to Appointments View
                if (enteredPass == null ? officialPassword == null : enteredPass.equals(officialPassword)) {
                    Utils.sceneChanger("view_controller/AppointmentsView.fxml", event);
                } else {
                    // If password does not match, throw error alert
                    Utils.throwErrorAlert("Your username or password is invalid.");
                } 
            } catch(IOException e) {
                System.out.println(e.getMessage());      
            }
        }      
    }
    
    // Close application
    public void loginExitButtonHandler(ActionEvent event) throws SQLException {
        Utils.exitApplication();
    }
    
    
    
}
