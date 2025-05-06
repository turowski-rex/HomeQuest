package com.homequest;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class PropertyDAO {

    public void updateDetails(Property property, String newLocation, BigDecimal newPrice, Integer newSize, Integer newNumberOfRooms, PropertyType newType) {
        System.out.println("Update details method in PropertyDAO needs database implementation.");
        property.setLocation(newLocation);
        property.setPrice(newPrice);
        property.setSize(newSize);
        property.setNumberOfRooms(newNumberOfRooms);
        property.setPropertyType(newType);
        System.out.println("Updated Details for:" + property.getPropertyID());
    }

    //get properties from database with filters
    public List<Property> getFilteredProperties(
            String location,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Integer minSize,
            Integer maxSize,
            Integer minRooms,
            Integer maxRooms,
            PropertyType propertyType,
            Boolean isForRent,
            Integer minRentDuration,
            Integer maxRentDuration,
            Boolean verificationStatus) {

        List<Property> properties = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT propertyID, sellerID, propertyName, location, price, size, numberOfRooms, propertyType, isForRent, rentDuration, verificationStatus FROM Properties WHERE 1=1");

        //add filters
        if (location != null && !location.isEmpty()) {
            sql.append(" AND location LIKE ?");
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
        }
         if (minSize != null) {
            sql.append(" AND size >= ?");
        }
        if (maxSize != null) {
            sql.append(" AND size <= ?");
        }
        if (minRooms != null) {
            sql.append(" AND numberOfRooms >= ?");
        }
         if (maxRooms != null) {
            sql.append(" AND numberOfRooms <= ?");
        }
        if (propertyType != null) {
            sql.append(" AND propertyType = ?");
        }
        if (isForRent != null) {
             sql.append(" AND isForRent = ?");
             if (isForRent) { //rent duration filter
                 if (minRentDuration != null) {
                     sql.append(" AND rentDuration >= ?");
                 }
                 if (maxRentDuration != null) {
                     sql.append(" AND rentDuration <= ?");
                 }
             }
        } else { //if isForRent is not specified, and rent duration is, filter by rent duration for rent properties
             if (minRentDuration != null) {
                 sql.append(" AND isForRent = TRUE AND rentDuration >= ?");
             }
             if (maxRentDuration != null) {
                  sql.append(" AND isForRent = TRUE AND rentDuration <= ?");
             }
        }
        if (verificationStatus != null) {
            sql.append(" AND verificationStatus = ?");
        }

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (location != null && !location.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + location + "%");
            }
            if (minPrice != null) {
                pstmt.setBigDecimal(paramIndex++, minPrice);
            }
            if (maxPrice != null) {
                pstmt.setBigDecimal(paramIndex++, maxPrice);
            }
             if (minSize != null) {
                pstmt.setInt(paramIndex++, minSize);
            }
            if (maxSize != null) {
                pstmt.setInt(paramIndex++, maxSize);
            }
             if (minRooms != null) {
                pstmt.setInt(paramIndex++, minRooms);
            }
             if (maxRooms != null) {
                pstmt.setInt(paramIndex++, maxRooms);
            }
            if (propertyType != null) {
                pstmt.setString(paramIndex++, propertyType.getDbValue());
            }
            if (isForRent != null) {
                 pstmt.setBoolean(paramIndex++, isForRent);
                  if (isForRent) {
                     if (minRentDuration != null) {
                         pstmt.setInt(paramIndex++, minRentDuration);
                     }
                     if (maxRentDuration != null) {
                         pstmt.setInt(paramIndex++, maxRentDuration);
                     }
                 }
            } else {
                 if (minRentDuration != null) {
                     //isForRent is in the SQL string already
                     pstmt.setInt(paramIndex++, minRentDuration);
                 }
                 if (maxRentDuration != null) {
                     pstmt.setInt(paramIndex++, maxRentDuration);
                 }
            }

            if (verificationStatus != null) {
                pstmt.setBoolean(paramIndex++, verificationStatus);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int propertyID = rs.getInt("propertyID");
                    int sellerID = rs.getInt("sellerID");
                    String propertyName = rs.getString("propertyName");
                    String propLocation = rs.getString("location");
                    BigDecimal price = rs.getBigDecimal("price");
                    Integer size = rs.getObject("size", Integer.class); //getObject for null int
                    Integer numberOfRooms = rs.getObject("numberOfRooms", Integer.class);
                    String propertyTypeString = rs.getString("propertyType");
                    PropertyType propType = null;
                    if (propertyTypeString != null) {
                        try {
                            propType = PropertyType.fromDbValue(propertyTypeString);
                        } catch (IllegalArgumentException e) {
                            System.err.println("Warning: Unknown PropertyType value in database: " + propertyTypeString);
                        }
                    }
                    Boolean isForRentValue = rs.getBoolean("isForRent");
                    Integer rentDurationValue = rs.getObject("rentDuration", Integer.class);
                    Boolean verificationStatusValue = rs.getBoolean("verificationStatus");

                    Property property = new Property(propertyID, sellerID, propertyName, propLocation, price,
                                               size, numberOfRooms, propType, isForRentValue,
                                               rentDurationValue, verificationStatusValue);
                    properties.add(property);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching filtered properties: " + e.getMessage());
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
                            // Depending on requirements, you might set a default, leave null, or skip this property
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
        return null;
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
                        // Create a new Property object with the generated ID
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
        return null;
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