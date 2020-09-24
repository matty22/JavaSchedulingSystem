
package dao;

import static dao.DBConnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Appointment;



public class AppointmentDao {
    
    // Get single appointment
    public static Appointment getAppointment(int appointmentId) throws SQLException {
    
         // Create SQL select statement using appointmentId
        String sqlStatement = "SELECT * FROM appointment WHERE appointmentId = ?";
        
        // Get reference to PreparedStatement
        DBQuery.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, appointmentId);
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        Appointment selectedAppointment = null;
        
        // Get Customer info from dB query
        while(rs.next()) {
            int appointmentIdNum = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            int userId = rs.getInt("userId");
            String title = rs.getString("title");
            String desc = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");
            String url = rs.getString("url");
            Timestamp startTime = rs.getTimestamp("start");
            Timestamp endTime = rs.getTimestamp("end");
            Timestamp createDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            
            selectedAppointment = new Appointment(appointmentIdNum, customerId, userId, title, 
                                                  desc, location, contact, type, url, startTime, 
                                                  endTime, createDate, createdBy, lastUpdate, 
                                                  lastUpdateBy);    
        }

        // Return appointment
        return selectedAppointment;
    }
    
    
    
    // Get all appointments
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        // Prepare Observable List variable to hold all users
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        
        // Create SQL select all users statement
        String sqlStatement = "SELECT * FROM appointment";
        
        // Get reference to PreparedStatement
        DBQuery.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        Appointment nextAppointment;
        
        // Get User info from dB query
        while(rs.next()) {
            int appointmentId = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            int userId = rs.getInt("userId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String appointmentType = rs.getString("type");
            String appointmentURL = rs.getString("url");
            Timestamp appointmentStart = rs.getTimestamp("start");
            Timestamp appointmentEnd = rs.getTimestamp("end");
            Timestamp createDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            
            nextAppointment = new Appointment(appointmentId, customerId, userId, title, description,
                                              location, contact, appointmentType, appointmentURL,
                                              appointmentStart, appointmentEnd, createDate, createdBy,
                                              lastUpdate, lastUpdateBy);  
            allAppointments.add(nextAppointment);
            
        }
        
        // Return observable list of appointments
        return allAppointments;
    }
    
    
    
    // Add new appointment to dB
    public static void createAppointment(int customerId, int userId, String title, String description,
                                         String location, String contact, String appointmentType, 
                                         String appointmentURL, Timestamp appointmentStart,
                                         Timestamp appointmentEnd, Timestamp createDate, String createdBy,
                                         Timestamp lastUpdate, String lastUpdateBy) throws SQLException {
        
        // Create SQL insert statement using Customer info
        String sqlStatement = "INSERT INTO appointment(customerId, userId, title, description,"
                              + "location, contact, appointmentType, appointmentURL, appointmentStart,"
                              + "appointmentEnd, createDate, createdBy, lastUpdate, lastUpdateBy)"
                              + "VALUES(?, ?, ?, '', '', '', ?, '', ?, ?, ?, ?, ?, ?)";
        
        // Get reference to PreparedStatement
        DBQuery.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, customerId);
        ps.setInt(2, userId);
        ps.setString(3, title);
        ps.setString(4, appointmentType);
        ps.setTimestamp(5, appointmentStart);
        ps.setTimestamp(6, appointmentEnd);
        ps.setTimestamp(7, createDate);
        ps.setString(8, createdBy);
        ps.setTimestamp(9, lastUpdate);
        ps.setString(10, lastUpdateBy);
        ps.execute();
    }
    
    
    
    // Update appointment
    public static void updateAppointment(int appointmentId, String appointmentType, 
                                         Timestamp appointmentStart, int customerId) throws SQLException {
    
        // Create SQL update statement
        String sqlStatement = "UPDATE appointment SET appointmentType = ?, appointmentStart = ?, "
                              + "customerId = ? WHERE appointmentId = ?";
        
        // Get reference to PreparedStatement
        DBQuery.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, appointmentType);
        ps.setTimestamp(2, appointmentStart);
        ps.setInt(3, customerId);
        ps.setInt(4, appointmentId);
        ps.execute();
        
    }
    
    
    
    // Delete appointment
    public static void deleteAppointment(int appointmentId) throws SQLException {
    
        // Create SQL delete statement
        String sqlStatement = "DELETE FROM appointment WHERE appointmentId = ?";
        
        // Get reference to PreparedStatement
        DBQuery.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setInt(1, appointmentId);
        ps.execute();
        
    }
}
