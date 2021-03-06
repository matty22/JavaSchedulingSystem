
package view_controller;

import dao.AddressDao;
import dao.CustomerDao;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Address;
import models.User;
import utilities.Utils;


public class AddCustomerViewController implements Initializable {

    // FXML variables for view controls
    @FXML private TextField customerNameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private Button cancelButton;
    @FXML private Button saveButton;
    
    User user;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    
    
    
    // Set current user of application
    public void initUser(User user) {
        this.user = user;
    }
    
    
    // Do nothing and return to Customer View
    public void cancelButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("AppointmentsView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
        AppointmentsViewController controller = loader.getController();
        controller.initUser(user);
        stage.show();
    }
    
    
    // Handle save button clicks
    public void saveButtonHandler(ActionEvent event) throws IOException, SQLException, InterruptedException {
        String customerName = customerNameField.getText();
        String address = addressField.getText();
        String phone = phoneField.getText();
        boolean phoneValid = Utils.checkPhoneNumbers(phone);
        
        
        // If the phone number is not valid, throw error
        if(!phoneValid) {
            Utils.throwErrorAlert("Phone number must be in valid format.");
        } else if(customerName.isEmpty()) {     // if customer name field is blank, throw error
            Utils.throwErrorAlert("Customer Name field must not be blank.");
        } else if(address.isEmpty()) {          // if address field is blank, throw error
            Utils.throwErrorAlert("Address field must not be blank.");
        } else {                                // if all fields are valid, add the new customer
            Timestamp timestamp = Timestamp.valueOf(LocalDate.now().atStartOfDay());
            int newAddressId = AddressDao.getAddressId();
            Address dBAddress = AddressDao.getAddress(address);
            
            // Don't add customer if they already exist
            if(CustomerDao.getCustomer(customerName) != null) {
                Utils.throwErrorAlert("Customer already exists.");
            } else {
                // If the address doesn't already exist, add it first, then add customer with the new address Id
                if(dBAddress == null) {
                    AddressDao.createAddress(address, "", 1, "", phone, timestamp, "admin", timestamp, "admin");
                    CustomerDao.createCustomer(customerName, newAddressId, 1, timestamp, "admin", timestamp, "admin"); 
                } else {
                    // If the address does exist, add new customer with existing Id
                    CustomerDao.createCustomer(customerName, dBAddress.getAddressId(), 1, timestamp, "admin", timestamp, "admin"); 
                }      
       
                FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("AppointmentsView.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setScene(scene);
        
                AppointmentsViewController controller = loader.getController();
                controller.initUser(user);
                stage.show();
        
            }
        }
    }
}
