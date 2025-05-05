package com.homequest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import java.sql.Statement;

@Repository
public class PropertyDAO {

    public void updateDetails(Property property, String newLocation, BigDecimal newPrice, Integer newSize, Integer newNumberOfRooms, PropertyType newType) {
        // future update - make this interact with database
        System.out.println("Update details method in PropertyDAO needs database implementation.");
        // Placeholder - does not use BD
        property.setLocation(newLocation);
        property.setPrice(newPrice);
        property.setSize(newSize);
        property.setNumberOfRooms(newNumberOfRooms);
        property.setPropertyType(newType);
        System.out.println("Updated Details for:" + property.getPropertyID());
    }

    //get all properties from DB
    public List<Property> getAllProperties() {
        List<Property> properties = new ArrayList<>();
        String sql = "SELECT propertyID, sellerID, propertyName, location, price, size, numberOfRooms, propertyType, isForRent, rentDuration, verificationStatus FROM Properties";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int propertyID = rs.getInt("propertyID");
                int sellerID = rs.getInt("sellerID");
                String propertyName = rs.getString("propertyName");
                String location = rs.getString("location");
                BigDecimal price = rs.getBigDecimal("price");
                Integer size = rs.getObject("size", Integer.class); //use getObject for null int
                Integer numberOfRooms = rs.getObject("numberOfRooms", Integer.class); //same here
                String propertyTypeString = rs.getString("propertyType");
                PropertyType propertyType = null;
                if (propertyTypeString != null) {
                    try {
                        propertyType = PropertyType.fromDbValue(propertyTypeString);
                    } catch (IllegalArgumentException e) {
                        System.err.println("Unknown PropertyType value in database: " + propertyTypeString);
                    }
                }
                Boolean isForRent = rs.getBoolean("isForRent");
                //check SQL for value = NULL before getting the int
                Integer rentDuration = rs.getObject("rentDuration", Integer.class);
                Boolean verificationStatus = rs.getBoolean("verificationStatus");

                Property property = new Property(propertyID, sellerID, propertyName, location, price,
                                               size, numberOfRooms, propertyType, isForRent,
                                               rentDuration, verificationStatus);
                properties.add(property);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching properties: " + e.getMessage());
            e.printStackTrace();
        }
        return properties;
    }

    //get a property by ID from DB
    public Property getPropertyById(int propertyID) {
        String sql = "SELECT propertyID, sellerID, propertyName, location, price, size, numberOfRooms, propertyType, isForRent, rentDuration, verificationStatus FROM Properties WHERE propertyID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int sellerID = rs.getInt("sellerID");
                    String propertyName = rs.getString("propertyName");
                    String location = rs.getString("location");
                    BigDecimal price = rs.getBigDecimal("price");
                    Integer size = rs.getObject("size", Integer.class);
                    Integer numberOfRooms = rs.getObject("numberOfRooms", Integer.class);
                    String propertyTypeString = rs.getString("propertyType");
                    PropertyType propertyType = null;
                    if (propertyTypeString != null) {
                        try {
                            propertyType = PropertyType.fromDbValue(propertyTypeString);
                        } catch (IllegalArgumentException e) {
                             System.err.println("Warning: Unknown PropertyType value in database: " + propertyTypeString);
                        }
                    }
                    Boolean isForRent = rs.getBoolean("isForRent");
                    Integer rentDuration = rs.getObject("rentDuration", Integer.class);
                    Boolean verificationStatus = rs.getBoolean("verificationStatus");

                    return new Property(propertyID, sellerID, propertyName, location, price,
                                        size, numberOfRooms, propertyType, isForRent,
                                        rentDuration, verificationStatus);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching property by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null; //null if property is not found or an error occurs
    }

    //add a new property to the database
    public Property createProperty(Property property) {
        String sql = "INSERT INTO Properties (sellerID, propertyName, location, price, size, numberOfRooms, propertyType, isForRent, rentDuration, verificationStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, property.getSellerID());
            pstmt.setString(2, property.getPropertyName());
            pstmt.setString(3, property.getLocation());
            pstmt.setBigDecimal(4, property.getPrice());
            
            // Handle nullable fields
            if (property.getSize() != null) {
                pstmt.setInt(5, property.getSize());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            if (property.getNumberOfRooms() != null) {
                pstmt.setInt(6, property.getNumberOfRooms());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }
            
            pstmt.setString(7, property.getPropertyType() != null ? property.getPropertyType().getDbValue() : null);
            pstmt.setBoolean(8, property.getIsForRent());

             if (property.getRentDuration() != null) {
                pstmt.setInt(9, property.getRentDuration());
            } else {
                pstmt.setNull(9, java.sql.Types.INTEGER);
            }
            
            pstmt.setBoolean(10, property.getVerificationStatus()); //init status is set in the Property object

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int propertyID = generatedKeys.getInt(1);
                        //make a new Property object with the generated ID
                        Property createdProperty = new Property(propertyID,
                                                              property.getSellerID(),
                                                              property.getPropertyName(),
                                                              property.getLocation(),
                                                              property.getPrice(),
                                                              property.getSize(),
                                                              property.getNumberOfRooms(),
                                                              property.getPropertyType(),
                                                              property.getIsForRent(),
                                                              property.getRentDuration(),
                                                              property.getVerificationStatus());
                        return createdProperty;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating property: " + e.getMessage());
            e.printStackTrace();
        }
        return null; //null if creation failed
    }

    //update an existing property in database
    public boolean updateProperty(int propertyID, Property property) {
        String sql = "UPDATE Properties SET sellerID = ?, propertyName = ?, location = ?, price = ?, size = ?, numberOfRooms = ?, propertyType = ?, isForRent = ?, rentDuration = ?, verificationStatus = ? WHERE propertyID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, property.getSellerID());
            pstmt.setString(2, property.getPropertyName());
            pstmt.setString(3, property.getLocation());
            pstmt.setBigDecimal(4, property.getPrice());

             //nullable fields
            if (property.getSize() != null) {
                pstmt.setInt(5, property.getSize());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            if (property.getNumberOfRooms() != null) {
                pstmt.setInt(6, property.getNumberOfRooms());
            } else {
                pstmt.setNull(6, java.sql.Types.INTEGER);
            }

            pstmt.setString(7, property.getPropertyType() != null ? property.getPropertyType().getDbValue() : null);
            pstmt.setBoolean(8, property.getIsForRent());

             if (property.getRentDuration() != null) {
                pstmt.setInt(9, property.getRentDuration());
            } else {
                pstmt.setNull(9, java.sql.Types.INTEGER);
            }

            pstmt.setBoolean(10, property.getVerificationStatus());
            pstmt.setInt(11, propertyID); //use propertyID from method parameter

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0; //true if at least one row was updated

        } catch (SQLException e) {
            System.err.println("Error updating property: " + e.getMessage());
            e.printStackTrace();
        }
        return false; //false if update failed
    }

    //delete a property from database by ID
    public boolean deleteProperty(int propertyID) {
        String sql = "DELETE FROM Properties WHERE propertyID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyID);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0; //true if at least row was deleted

        } catch (SQLException e) {
            System.err.println("Error deleting property: " + e.getMessage());
            e.printStackTrace();
        }
        return false; //false if deletion failed/property not found
    }

}