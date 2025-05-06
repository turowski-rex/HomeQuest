package com.homequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date; // for DateOfBirth
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class SellerDAO {

    //adding new seller to the database = registration
    public Seller createSeller(Seller seller) {
        String sql = "INSERT INTO Sellers (FullName, Email, Password, DateOfBirth, ConsentLocation, ProfilePhoto, PhoneNumber, UserType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, seller.getFullName());
            pstmt.setString(2, seller.getEmail());
            pstmt.setString(3, seller.getPassword());
            pstmt.setDate(4, seller.getDateOfBirth()); //can be null
            
            if (seller.getConsentLocation() != null) {
                 pstmt.setBoolean(5, seller.getConsentLocation());
            } else {
                 pstmt.setNull(5, java.sql.Types.BOOLEAN);
            }
            pstmt.setString(6, seller.getProfilePhoto()); //can be null
            pstmt.setString(7, seller.getPhoneNumber()); //can be null
            pstmt.setString(8, seller.getUserType()); //default 'Free'

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int sellerID = generatedKeys.getInt(1);
                        // new Seller with generated ID
                        Seller createdSeller = new Seller(sellerID,
                            seller.getFullName(),
                            seller.getEmail(),
                            seller.getDateOfBirth(),
                            seller.getConsentLocation(),
                            seller.getProfilePhoto(),
                            seller.getPhoneNumber(),
                            seller.getUserType());
                        return createdSeller;
                    }
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating seller: " + e.getMessage());
             if (e.getMessage().toLowerCase().contains("duplicate entry")) {
                 System.err.println("Registration failed: Email '" + seller.getEmail() + "' already exists.");
             }
            e.printStackTrace();
        }
        return null; //null if registration fail
    }

    //get seller by ID
    public Seller getSellerById(int sellerID) {
        String sql = "SELECT SellerID, FullName, Email, DateOfBirth, ConsentLocation, ProfilePhoto, PhoneNumber, UserType FROM Sellers WHERE SellerID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sellerID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("FullName");
                    String email = rs.getString("Email");
                    Date dateOfBirth = rs.getDate("DateOfBirth");
                    Boolean consentLocation = rs.getBoolean("ConsentLocation");
                    if (rs.wasNull()) consentLocation = null;
                    String profilePhoto = rs.getString("ProfilePhoto");
                    String phoneNumber = rs.getString("PhoneNumber");
                    String userType = rs.getString("UserType");

                    return new Seller(sellerID, fullName, email, dateOfBirth, consentLocation, profilePhoto, phoneNumber, userType);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching seller by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null; //null if seller not found
    }

    //get a seller by email and password (for login)
    public Seller getSellerByEmailAndPassword(String email, String password) {
         String sql = "SELECT SellerID, FullName, Email, DateOfBirth, ConsentLocation, ProfilePhoto, PhoneNumber, UserType FROM Sellers WHERE Email = ? AND Password = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int sellerID = rs.getInt("SellerID");
                    String fullName = rs.getString("FullName");
                    String dbEmail = rs.getString("Email");
                    Date dateOfBirth = rs.getDate("DateOfBirth");
                    Boolean consentLocation = rs.getBoolean("ConsentLocation");
                     if (rs.wasNull()) consentLocation = null; 
                    String profilePhoto = rs.getString("ProfilePhoto");
                    String phoneNumber = rs.getString("PhoneNumber");
                    String userType = rs.getString("UserType");

                    return new Seller(sellerID, fullName, dbEmail, dateOfBirth, consentLocation, profilePhoto, phoneNumber, userType);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching seller by email and password: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    //update existing seller profile in db
    //Does NOT update email, password, userType
    public boolean updateSellerProfile(int sellerID, Seller seller) {
        String sql = "UPDATE Sellers SET FullName = ?, DateOfBirth = ?, ConsentLocation = ?, ProfilePhoto = ?, PhoneNumber = ? WHERE SellerID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, seller.getFullName());
            pstmt.setDate(2, seller.getDateOfBirth());
            if (seller.getConsentLocation() != null) {
                 pstmt.setBoolean(3, seller.getConsentLocation());
            } else {
                 pstmt.setNull(3, java.sql.Types.BOOLEAN);
            }
            pstmt.setString(4, seller.getProfilePhoto());
            pstmt.setString(5, seller.getPhoneNumber());
            pstmt.setInt(6, sellerID);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0; //true if at least one row updated

        } catch (SQLException e) {
            System.err.println("Error updating seller profile: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    //update seller from Free to Gold
    public boolean updateSellerUserType(int sellerID, String newUserType) {
         if (!"Free".equals(newUserType) && !"Gold".equals(newUserType)) {
            System.err.println("Invalid user type: " + newUserType + ". Must be 'Free' or 'Gold'.");
            return false;
        }

        String sql = "UPDATE Sellers SET UserType = ? WHERE SellerID = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, newUserType);
            pstmt.setInt(2, sellerID);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("SQL Error updating seller user type: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //delete seller by ID
    public boolean deleteSeller(int sellerID) {
        String sql = "DELETE FROM Sellers WHERE SellerID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, sellerID);

            int affectedRows = pstmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting seller: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
