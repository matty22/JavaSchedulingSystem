
package view_controller;

import dao.AppointmentDao;
import dao.CustomerDao;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.AppointmentShort;
import models.CustomerShort;
import models.User;
import utilities.Utils;


public class AppointmentsViewController implements Initializable {

    // FXML variables for Appointment View controls
    @FXML private Button addApptButton;
    @FXML private Button editApptButton;
    @FXML private RadioButton viewAllRadio;
    @FXML private RadioButton viewWeekRadio;
    @FXML private RadioButton viewMonthRadio;
    @FXML private TableView<AppointmentShort> appointmentsTable;
    @FXML private TableColumn<AppointmentShort, String> appointmentDateColumn;
    @FXML private TableColumn<AppointmentShort, String> appointmentTypeColumn;
    @FXML private TableColumn<AppointmentShort, Integer> appointmentCustomerColumn;
        
    // FXML variables for Customer View controls
    @FXML private Button newCustomerButton;
    @FXML private Button editCustomerButton;
    @FXML private TableView<CustomerShort> customerTable;
    @FXML private TableColumn<CustomerShort, String> customerNameColumn;
    @FXML private TableColumn<CustomerShort, String> customerAddressColumn;
    @FXML private TableColumn<CustomerShort, String> customerPhoneColumn;
 
    // FXML variables for Reports View controls
    @FXML private Button apptTypesReportButton;
    @FXML private Button consultantScheduleReportButton;
    @FXML private Button oneOtherReportButton;
    @FXML private Button loginReportButton;
    
    User user;
    int userId;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
       // Bind customer table columns
       customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
       customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
       customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
       
       // Populate customer table view
        try {
            ObservableList<CustomerShort> customers = CustomerDao.getAllCustomers();
            customerTable.setItems(customers);
        }
         catch (SQLException ex) {
            Logger.getLogger(AppointmentsViewController.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }   
    
    
    // Set current user of application
    public void initUser(User user) {
        this.user = user;
        this.userId = user.getUserId();
        populateAppointmentTable();
    }
    
    
    public void populateAppointmentTable() {
       
        // Bind appointment table columns
        appointmentCustomerColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
       
        try {
            // Populate appointments table view
            ObservableList<AppointmentShort> appointments = AppointmentDao.getAllAppointmentsByUser(this.userId);
            appointmentsTable.setItems(appointments);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // Check to see if current user has appointment within 15 minutes of login
    public void upcomingAppointments(User user) throws SQLException, InterruptedException {
        ObservableList<Timestamp> upcomingAppointmentTimes = AppointmentDao.getAllAppointmentTimesByUser(this.userId);
        
        // Get Instant for now and 15 mins from now
        Instant now = Instant.now();
        Instant fifteenAhead = now.plusSeconds(900);
        
        // Convert to Timestamps to compare to Timestamps from dB call
        Timestamp nowTS = Timestamp.from(Instant.now());
        Timestamp fifteenAheadTS = Timestamp.from(fifteenAhead);
       
        //Check each appointment timestamp to see if it is after 'now' but before 15 minutes from 'now'
        //********* Here's an example of a place where I chose not to use a lambda expression
        //********* The lambda expression is more verbose and harder to comprehend than the loop
        for(Timestamp ts : upcomingAppointmentTimes) {
            Timestamp localTime = Utils.fromUTC(ts);
            if(localTime.compareTo(nowTS) > 0 && localTime.compareTo(fifteenAheadTS) <= 0) {
                Utils.throwUpcomingAppointmentAlert(localTime);
            }
        }
    }
    

    // Change scene to Add Appt View
    public void addApptButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("AddAppointmentView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        AddAppointmentViewController controller = loader.getController();
        controller.initUser(user);
        stage.show();
    }
    
    // Handle clicks on edit appointment button
    public Stage editApptButtonHandler(ActionEvent event) throws IOException {
        
        // Open Edit Appointment view and pass selected appointment through
        AppointmentShort appointment = appointmentsTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("EditDeleteAppointmentsView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
        EditDeleteAppointmentsViewController controller = loader.getController();
        controller.initAppointment(appointment);
        controller.initUser(user);
        stage.show();
        return stage;
    }
    
    // If no appointment selected, disable edit appointment button
    public void toggleEditApptButton() {
        if(appointmentsTable.getSelectionModel().getSelectedItem() != null) {
            editApptButton.setDisable(false);
        } else {
            editApptButton.setDisable(true);
        }
    }
    
    // If no customer selected, disable edit customer button
    public void toggleEditCustButton() {
        if(customerTable.getSelectionModel().getSelectedItem() != null) {
            editCustomerButton.setDisable(false);
        } else {
            editCustomerButton.setDisable(true);
        }
    }
    
    
    // Show all appointments in Appointments table view
    public void viewAllHandler(ActionEvent event) {
        try {
            ObservableList<AppointmentShort> appointments = AppointmentDao.getAllAppointmentsByUser(this.userId);
            appointmentsTable.setItems(appointments);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentsViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    // Show only appointments in the current week in Appointments table view
    public void viewByWeekHandler(ActionEvent event) throws SQLException {
        Calendar cal = Calendar.getInstance();
        int thisWeek = cal.get(Calendar.WEEK_OF_YEAR);
        int thisYear = cal.get(Calendar.YEAR);
        ObservableList<AppointmentShort> appointments = AppointmentDao.getAllAppointmentsByUser(this.userId);
        ObservableList<AppointmentShort> apptsToRemove = FXCollections.observableArrayList();
        
        //********* Using lambda expression here rather than for loop 
        //********* for conciseness and easier comprehension
        appointments.forEach(appointment -> {
            Calendar calendar = Calendar.getInstance();
            Timestamp timestamp = Utils.fromUTC(appointment.getAppointmentStart());
            Date date = Date.valueOf(timestamp.toString().substring(0, 10));
            calendar.setTime(date);
            if(calendar.get(Calendar.YEAR) != thisYear || calendar.get(Calendar.WEEK_OF_YEAR)!= thisWeek) {
                apptsToRemove.add(appointment);
            }
        });
        appointments.removeAll(apptsToRemove);
        appointmentsTable.setItems(appointments);
        
    }
    
    
    // Show only appointments in the current month in Appointments table view
    public void viewByMonthHandler(ActionEvent event) throws SQLException {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        String yearMonth = String.valueOf(year) + "-" + String.valueOf(month);
        ObservableList<AppointmentShort> appointments = AppointmentDao.getAllAppointmentsByUser(this.userId);
        ObservableList<AppointmentShort> apptsToRemove = FXCollections.observableArrayList();

        //********* Using lambda expression here rather than for loop 
        //********* for conciseness and easier comprehension
        appointments.forEach(appointment -> {
           Timestamp start = Utils.fromUTC(appointment.getAppointmentStart());
           String timeString = start.toString().substring(0, 7);
            if(!timeString.equals(yearMonth)) {
                apptsToRemove.add(appointment);
            }
        });
        appointments.removeAll(apptsToRemove);
        appointmentsTable.setItems(appointments);
    }
    
    
    // Change scene to Add Customer View
    public void addCustomerButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("AddCustomerView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
        AddCustomerViewController controller = loader.getController();
        controller.initUser(user);
        stage.show();
    }
    
    
    // Open Edit Customer view and pass selected customer through
    public Stage editCustomerButtonHandler(ActionEvent event) throws IOException {
        CustomerShort customer = customerTable.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("EditDeleteCustomerView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
        EditDeleteCustomerViewController controller = loader.getController();
        controller.initCustomer(customer);
        controller.initUser(user);
        stage.show();
        return stage;
    }
    
   
    // Open appointment type by month report on button click
    public void apptTypesByMonthReportButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("AppointmentsByMonthReportView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
        AppointmentsByMonthReportViewController controller = loader.getController();
        controller.initUser(user);
        stage.show();
    }
    
    
    // Open consultant schedule report on button click
    public void consultantScheduleReportButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("ConsultantScheduleReportView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
        ConsultantScheduleReportViewController controller = loader.getController();
        controller.initUser(user);
        stage.show();
    }
    
    
    // Open most appearnaces report on button click
    public void mostAppearancesReportButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("MostAppearancesReportView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
        MostAppearancesReportViewController controller = loader.getController();
        controller.initUser(user);
        stage.show();
    }
    
    
    // Open login report on button click
    public void loginReportButtonHandler(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                                           .getResource("LoginReportView.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        
        LoginReportViewController controller = loader.getController();
        controller.initUser(user);
        stage.show();
    }
}
