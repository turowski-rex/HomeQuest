package com.homequest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
}