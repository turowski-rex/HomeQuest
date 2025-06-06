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

    public boolean updateDetails(int propertyID, String newLocation, BigDecimal newPrice, Integer newSize, Integer newNumberOfRooms, PropertyType newType) {
        String sql = "UPDATE Properties SET location = ?, price = ?, size = ?, numberOfRooms = ?, propertyType = ? WHERE propertyID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newLocation);
            pstmt.setBigDecimal(2, newPrice);
            
            // Handle nullable fields
            if (newSize != null) {
                pstmt.setInt(3, newSize);
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }
            if (newNumberOfRooms != null) {
                pstmt.setInt(4, newNumberOfRooms);
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }

            pstmt.setString(5, newType != null ? newType.getDbValue() : null);
            pstmt.setInt(6, propertyID);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating property details: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //get properties from the database with filtering
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
            Boolean verificationStatus,
            Integer sellerID) {

        List<Property> properties = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.propertyID, p.sellerID, p.propertyName, p.location, p.price, p.size, p.numberOfRooms, p.propertyType, p.isForRent, p.rentDuration, p.verificationStatus, pi.imagePath FROM Properties p LEFT JOIN PropertyImages pi ON p.propertyID = pi.propertyID WHERE 1=1");

        //sellerID filter
        if (sellerID != null) {
            sql.append(" AND p.sellerID = ?");
        }
        //filters of parameters
        if (location != null && !location.isEmpty()) {
            sql.append(" AND p.location LIKE ?");
        }
        if (minPrice != null) {
            sql.append(" AND p.price >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND p.price <= ?");
        }
         if (minSize != null) {
            sql.append(" AND p.size >= ?");
        }
        if (maxSize != null) {
            sql.append(" AND p.size <= ?");
        }
        if (minRooms != null) {
            sql.append(" AND p.numberOfRooms >= ?");
        }
         if (maxRooms != null) {
            sql.append(" AND p.numberOfRooms <= ?");
        }
        if (propertyType != null) {
            sql.append(" AND p.propertyType = ?");
        }
        if (isForRent != null) {
             sql.append(" AND p.isForRent = ?");
             if (isForRent) {
                 if (minRentDuration != null) {
                     sql.append(" AND p.rentDuration >= ?");
                 }
                 if (maxRentDuration != null) {
                     sql.append(" AND p.rentDuration <= ?");
                 }
             }
        } else { //if isForRent is not specified, and rent duration is, filter by rent duration for rent properties
             if (minRentDuration != null) {
                 sql.append(" AND p.isForRent = TRUE AND p.rentDuration >= ?");
             }
             if (maxRentDuration != null) {
                  sql.append(" AND p.isForRent = TRUE AND p.rentDuration <= ?");
             }
        }
        if (verificationStatus != null) {
            sql.append(" AND p.verificationStatus = ?");
        }

        //group by property to handle multiple images per property
        sql.append(" GROUP BY p.propertyID, p.sellerID, p.propertyName, p.location, p.price, p.size, p.numberOfRooms, p.propertyType, p.isForRent, p.rentDuration, p.verificationStatus, pi.imagePath");

        //order by PropertyID
        sql.append(" ORDER BY p.propertyID");

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIndex = 1;
            if (location != null && !location.isEmpty()) {
                pstmt.setString(paramIndex++, "%" + location + "%");
            }
            if (sellerID != null) {
                pstmt.setInt(paramIndex++, sellerID);
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
                    int propSellerID = rs.getInt("sellerID");
                    String propertyName = rs.getString("propertyName");
                    String propLocation = rs.getString("location");
                    BigDecimal price = rs.getBigDecimal("price");
                    Integer size = rs.getObject("size", Integer.class); // getObject for null int
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
                    Integer rentDurationValue = rs.getObject("rentDuration", Integer.class); // Use getObject for nullable INT
                    Boolean verificationStatusValue = rs.getBoolean("verificationStatus");

                    Property property = new Property(propertyID, propSellerID, propertyName, propLocation, price,
                                               size, numberOfRooms, propType, isForRentValue,
                                               rentDurationValue, verificationStatusValue);
                                               
                    //image paths for property
                    List<String> imagePaths = getImagePathsForProperty(propertyID);
                    property.setImagePaths(imagePaths);

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
        if (propertyID <= 0) {
            System.err.println("Invalid propertyID for fetching property.");
            return null;
        }
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

                    Property property = new Property(propertyID, sellerID, propertyName, location, price,
                                        size, numberOfRooms, propertyType, isForRent,
                                        rentDuration, verificationStatus);
                    
                    List<String> imagePaths = getImagePathsForProperty(propertyID);
                    property.setImagePaths(imagePaths);

                    return property;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching property by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //add new property to the database
    public Property createProperty(Property property) {
        //validation
        if (property.getSellerID() <= 0 || property.getPropertyName() == null || property.getPropertyName().trim().isEmpty() ||
            property.getLocation() == null || property.getLocation().trim().isEmpty() || property.getPrice() == null) {
            System.err.println("Invalid data for property creation.");
            return null;
        }
        if (property.getIsForRent() && property.getRentDuration() == null) {
             System.err.println("Rent duration is required for rental properties.");
             return null;
        }

        String sql = "INSERT INTO Properties (sellerID, propertyName, location, price, size, numberOfRooms, propertyType, isForRent, rentDuration, verificationStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, property.getSellerID());
            pstmt.setString(2, property.getPropertyName());
            pstmt.setString(3, property.getLocation());
            pstmt.setBigDecimal(4, property.getPrice());

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

    // Method to update an existing property in the database
    public boolean updateProperty(int propertyID, Property property) {
         if (propertyID <= 0) {
             System.err.println("Invalid propertyID for property update.");
             return false;
         }
         // Basic validation for the property object
        if (property.getSellerID() <= 0 || property.getPropertyName() == null || property.getPropertyName().trim().isEmpty() ||
            property.getLocation() == null || property.getLocation().trim().isEmpty() || property.getPrice() == null) {
            System.err.println("Invalid data in property object for update.");
            return false;
        }
        String sql = "UPDATE Properties SET sellerID = ?, propertyName = ?, location = ?, price = ?, size = ?, numberOfRooms = ?, propertyType = ?, isForRent = ?, rentDuration = ?, verificationStatus = ? WHERE propertyID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

            pstmt.setBoolean(10, property.getVerificationStatus());
            pstmt.setInt(11, propertyID); // Use the propertyID from the method parameter

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error updating property: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //delete a property from database by ID
    public boolean deleteProperty(int propertyID) {
        if (propertyID <= 0) {
             System.err.println("Invalid propertyID for property deletion.");
             return false;
         }
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
        return false;
    }

    //add an image path for property to the database
    public boolean addImagePath(int propertyID, String imagePath) {
         if (propertyID <= 0 || imagePath == null || imagePath.trim().isEmpty()) {
             System.err.println("Invalid propertyID or imagePath for adding image.");
             return false;
         }
        String sql = "INSERT INTO PropertyImages (propertyID, imagePath) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyID);
            pstmt.setString(2, imagePath);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0; //true if insertion successful

        } catch (SQLException e) {
            System.err.println("Error adding image path for property " + propertyID + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //get image paths for a property
    public List<String> getImagePathsForProperty(int propertyID) {
        if (propertyID <= 0) {
             System.err.println("Invalid propertyID for fetching image paths.");
             return new ArrayList<>();
         }
        List<String> imagePaths = new ArrayList<>();
        String sql = "SELECT imagePath FROM PropertyImages WHERE propertyID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, propertyID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    imagePaths.add(rs.getString("imagePath"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching image paths for property " + propertyID + ": " + e.getMessage());
            e.printStackTrace();
        }
        return imagePaths;
    }

    //get all properties for a specific seller
    public List<Property> getPropertiesBySellerId(int sellerID) {
         if (sellerID <= 0) {
             System.err.println("Invalid sellerID for fetching properties.");
             return new ArrayList<>();
         }
        List<Property> properties = new ArrayList<>();
        String sql = "SELECT p.propertyID, p.sellerID, p.propertyName, p.location, p.price, p.size, p.numberOfRooms, p.propertyType, p.isForRent, p.rentDuration, p.verificationStatus, pi.imagePath FROM Properties p LEFT JOIN PropertyImages pi ON p.propertyID = pi.propertyID WHERE p.sellerID = ? GROUP BY p.propertyID, p.sellerID, p.propertyName, p.location, p.price, p.size, p.numberOfRooms, p.propertyType, p.isForRent, p.rentDuration, p.verificationStatus, pi.imagePath ORDER BY p.propertyID";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sellerID);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                     properties.add(new Property(
                        rs.getInt("propertyID"),
                        rs.getInt("sellerID"),
                        rs.getString("propertyName"),
                        rs.getString("location"),
                        rs.getBigDecimal("price"),
                        rs.getObject("size", Integer.class),
                        rs.getObject("numberOfRooms", Integer.class),
                        PropertyType.fromDbValue(rs.getString("propertyType")),
                        rs.getBoolean("isForRent"),
                        rs.getObject("rentDuration", Integer.class),
                        rs.getBoolean("verificationStatus")));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching properties for seller ID " + sellerID + ": " + e.getMessage());
        }
        return properties;
    }
}