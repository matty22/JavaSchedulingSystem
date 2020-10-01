
package dao;

import static dao.DBConnection.conn;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import models.Address;


public class AddressDao {
    
    // Get single Address
    public static Address getAddress(String address) throws SQLException {
    
         // Create SQL select statement using address string
        String sqlStatement = "SELECT * FROM address WHERE address = ?";
        
        // Get reference to PreparedStatement
        DBQuery.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        ps.setString(1, address);
        ps.execute();
        
        ResultSet rs = ps.getResultSet();
        
        Address selectedAddress = null;
        
        // Get City info from dB query
        while(rs.next()) {
            int addressId = rs.getInt("addressId");
            String address1 = rs.getString("address");
            String address2 = rs.getString("address2");
            int cityId = rs.getInt("cityId");
            String postalCode = rs.getString("postalCode");
            String phone = rs.getString("phone");
            Timestamp createDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");

            
            selectedAddress = new Address(addressId, address1, address2, cityId, postalCode,
                                          phone, createDate, createdBy,lastUpdate, lastUpdateBy);    
        }

        // Return address
        return selectedAddress;
    }
    
    
    
    // Add new address to dB
    public static void createAddress(String address, String address2, int cityId, 
                                     String postalCode, String phone, Timestamp createDate, 
                                     String createdBy, Timestamp lastUpdate, String lastUpdateBy) throws SQLException {
        
        // Create SQL insert statement using address info
        String sqlStatement = "INSERT INTO address(address, address2, cityId, postalCode,"
                                                  + "phone, createDate, createdBy,"
                                                + " lastUpdate, lastUpdateBy) "
                                                + "VALUES(?, '', ?, ?, ?, ?, ?, ?, ?)";
        
        // Get reference to PreparedStatement
        DBQuery.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps2 = DBQuery.getPreparedStatement();
        ps2.setString(1, address);
        ps2.setInt(2, cityId);
        ps2.setString(3, postalCode);
        ps2.setString(4, phone);
        ps2.setTimestamp(5, createDate);
        ps2.setString(6, createdBy);
        ps2.setTimestamp(7, lastUpdate);
        ps2.setString(8, lastUpdateBy);
        ps2.execute();
    }
    
    // Get single Address
    public static int getAddressId() throws SQLException {
    
         String sqlStatement = "SELECT max(addressId)+1 FROM address";
        
        // Get reference to PreparedStatement
        DBQuery.setPreparedStatement(conn, sqlStatement);
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        // Get next available addressId integer
        ResultSet rs = ps.executeQuery();
        String nextAddressId = "";
        while (rs.next()) {
            nextAddressId = rs.getString(1);
        }
        
        return Integer.parseInt(nextAddressId);
    }
}
