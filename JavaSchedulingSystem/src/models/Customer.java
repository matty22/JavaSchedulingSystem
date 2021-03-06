
package models;

// Object model for application customers

import java.sql.Timestamp;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer {
    
    // Customer Object fields
    private SimpleIntegerProperty customerId = new SimpleIntegerProperty();
    private SimpleStringProperty customerName = new SimpleStringProperty();
    private SimpleIntegerProperty addressId = new SimpleIntegerProperty();
    private SimpleIntegerProperty active = new SimpleIntegerProperty();
    private Timestamp createDate;
    private SimpleStringProperty createdBy = new SimpleStringProperty();
    private Timestamp lastUpdate;
    private SimpleStringProperty lastUpdateBy = new SimpleStringProperty();
    
    
    // Customer Constructor
    public Customer(int customerId, String customerName, int addressId, int active, 
                    Timestamp createDate, String createdBy, Timestamp lastUpdate, String lastUpdateBy) {
        setCustomerId(customerId);
        setCustomerName(customerName);
        setCustomerAddressId(addressId);
        setCustomerActive(active);
        setCustomerCreateDate(createDate);
        setCustomerCreatedBy(createdBy);
        setCustomerLastUpdate(lastUpdate);
        setCustomerLastUpdateBy(lastUpdateBy);
    }
    
    
    // CustomerID Getter and Setter
    public int getCustomerId() {
        return customerId.get();
    }
    
    public void setCustomerId(int customerId) {
        this.customerId.set(customerId);
    }
    
    
    // First Name Getter and Setter
    public String getCustomerName() {
        return customerName.get();
    }
    
    public void setCustomerName(String customerName) {
        this.customerName.set(customerName);
    }
    
    
    // Address Getter and Setter
    public int getCustomerAddressId() {
        return addressId.get();
    }
    
    public void setCustomerAddressId(int addressId) {
        this.addressId.set(addressId);
    }
    
    
    // Active Getter and Setter
    public int getCustomerActive() {
        return active.get();
    }
    
    public void setCustomerActive(int active) {
        this.active.set(active);
    }
    
    
    // Create Date Getter and Setter
    public Timestamp getCustomerCreateDate() {
        return createDate;
    }
    
    public void setCustomerCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }
    
    
    // Created By Getter and Setter
    public String getCustomerCreatedBy() {
        return createdBy.get();
    }
    
    public void setCustomerCreatedBy(String createdBy) {
        this.createdBy.set(createdBy);
    }
    
    
    // Last Update Getter and Setter
    public Timestamp getCustomerLastUpdate() {
        return lastUpdate;
    }
    
    public void setCustomerLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    
    // Last Updated By Getter and Setter
    public String getCustomerLastUpdateBy() {
        return lastUpdateBy.get();
    }
    
    public void setCustomerLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy.set(lastUpdateBy);
    }
}
