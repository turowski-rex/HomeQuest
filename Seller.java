import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date; // for DateOfBirth

public class Seller {

    //Sellers table columns
    private int sellerID; // Primary Key, auto-incremented
    private String fullName;
    private String email;
    private String password; // plain text password - change to hash?
    private Date dateOfBirth;
    private Boolean consentLocation;
    private String profilePhoto; // Path
    private String phoneNumber;
    private String userType; // 'Free' or 'Gold'

    // Transient state (not stored in DB directly)
    private boolean loggedIn = false;

    // constructor loading an existing seller (after login)
    // use when retrieving seller data from the database
    public Seller(int sellerID, String fullName, String email, Date dateOfBirth,
                  Boolean consentLocation, String profilePhoto, String phoneNumber, String userType) {
        this.sellerID = sellerID;
        this.fullName = fullName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.consentLocation = consentLocation;
        this.profilePhoto = profilePhoto;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        // Password is not loaded back into the object after login
        this.password = null; 
    }

    // constructor for registration process (before sellerID is known)
    // creating a new Seller object before inserting into DB
    public Seller(String fullName, String email, String password, Date dateOfBirth, Boolean consentLocation, String profilePhoto, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.password = password; // plain text password
        this.dateOfBirth = dateOfBirth;
        this.consentLocation = consentLocation;
        this.profilePhoto = profilePhoto;
        this.phoneNumber = phoneNumber;
        this.userType = "Free"; // Default to Free on registration
    }


    //
    // register new seller in the database with current object state
     //return true if registration is successful
    public boolean register() {
        // Prevent registration if object already has an ID (= it came from DB)
        if (this.sellerID > 0) {
            System.err.println("Cannot register an already existing seller (ID: " + this.sellerID + ").");
            return false;
        }
        // Check if essential registration data is present (email, password, name)
        if (this.email == null || this.email.isEmpty() || 
            this.password == null || this.password.isEmpty() ||
            this.fullName == null || this.fullName.isEmpty()){
            System.err.println("Registration failed: Full name, email, and password are required.");
            return false;
        }

        String sql = "INSERT INTO Sellers (FullName, Email, Password, DateOfBirth, ConsentLocation, ProfilePhoto, PhoneNumber, UserType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet generatedKeys = null;

        try {
            conn = DatabaseConnector.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, this.fullName);
            pstmt.setString(2, this.email);
            pstmt.setString(3, this.password); // Storing plain text password
            pstmt.setDate(4, this.dateOfBirth); // Can be null if not provided - optional
            
            if (this.consentLocation != null) {
                 pstmt.setBoolean(5, this.consentLocation);
            } else {
                 pstmt.setNull(5, java.sql.Types.BOOLEAN);
            }
            pstmt.setString(6, this.profilePhoto); // Can be null
            pstmt.setString(7, this.phoneNumber); // Can be null
            pstmt.setString(8, this.userType); // Default 'Free'

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    this.sellerID = generatedKeys.getInt(1); // Set the ID on the object
                    System.out.println("Seller '" + this.fullName + "' registered successfully with ID: " + this.sellerID);
                    this.password = null; // Clear password after successful registration
                    return true;
                } else {
                     System.err.println("Failed to retrieve generated SellerID after insertion.");
                    return false;
                }
            } else {
                System.err.println("Seller registration failed (no rows affected). Possible issue with DB constraints or connection.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("SQL Error during seller registration: " + e.getMessage());
             if (e.getMessage().toLowerCase().contains("duplicate entry")) {
                 System.err.println("Registration failed: Email '" + this.email + "' already exists.");
             }
            // e.printStackTrace(); //print stack trace - debugging
            return false;
        } finally {
             try { if (generatedKeys != null) generatedKeys.close(); } catch (SQLException e) { /* ignore */ }
             try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignore */ }
             // Connection is managed by DatabaseConnector, do not close here
        }
    }

    
     // method to log in a seller - verifying email and plain text password
     // email = seller's email
     // password = seller's plain text password
     // returns The fully populated Seller object if login is successful, null otherwise
    public static Seller login(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
             System.err.println("Login failed: Email and password cannot be empty.");
             return null;
        }
        
        String sql = "SELECT SellerID, FullName, Email, DateOfBirth, ConsentLocation, ProfilePhoto, PhoneNumber, UserType FROM Sellers WHERE Email = ? AND Password = ?"; 
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Seller seller = null;

        try {
            conn = DatabaseConnector.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password); // campare plain text password

            rs = pstmt.executeQuery();

            if (rs.next()) {
                // log-in successful, create Seller from database data
                int sellerID = rs.getInt("SellerID");
                String fullName = rs.getString("FullName");
                String dbEmail = rs.getString("Email"); 
                Date dateOfBirth = rs.getDate("DateOfBirth");
                Boolean consentLocation = rs.getBoolean("ConsentLocation");
                 if (rs.wasNull()) consentLocation = null; 
                String profilePhoto = rs.getString("ProfilePhoto");
                String phoneNumber = rs.getString("PhoneNumber");
                String userType = rs.getString("UserType");

                seller = new Seller(sellerID, fullName, dbEmail, dateOfBirth, consentLocation, profilePhoto, phoneNumber, userType);
                seller.setLoggedIn(true); 
                System.out.println("Seller '" + fullName + "' logged in successfully.");

            } else {
                System.out.println("Login failed for email '" + email + "'. Invalid email or password.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during seller login: " + e.getMessage());
            // e.printStackTrace(); // debugging
        } finally {
             try { if (rs != null) rs.close(); } catch (SQLException e) { /* ignore */ }
             try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignore */ }
             // Connection is managed by DatabaseConnector, do not close here
        }
        return seller;
    }

     //Log-out current seller (sets loggedIn = false).
    public void logout() {
        if (!this.loggedIn) {
             System.out.println("Seller '" + (this.fullName != null ? this.fullName : this.email) + "' is not logged in.");
             return;
        }
        this.loggedIn = false;
        System.out.println("Seller '" + (this.fullName != null ? this.fullName : this.email) + "' has logged out.");
        // No DB action needed for logout
    }


    // --- Database Update Methods

    
     // Updat seller info in the database
     // Call this method AFTER modifying fields like fullName, dob, photo, phone, consent
     // Does NOT update email, password, or userType
     
     // returns true if the update was successful, false otherwise

    public boolean updateProfileInDB() {
        if (this.sellerID <= 0) {
             System.err.println("Cannot update profile for a seller without a valid ID.");
             return false;
        }
        String sql = "UPDATE Sellers SET FullName = ?, DateOfBirth = ?, ConsentLocation = ?, ProfilePhoto = ?, PhoneNumber = ? WHERE SellerID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnector.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, this.fullName);
            pstmt.setDate(2, this.dateOfBirth);
            if (this.consentLocation != null) {
                 pstmt.setBoolean(3, this.consentLocation);
            } else {
                 pstmt.setNull(3, java.sql.Types.BOOLEAN);
            }
            pstmt.setString(4, this.profilePhoto);
            pstmt.setString(5, this.phoneNumber);
            pstmt.setInt(6, this.sellerID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Seller profile updated successfully for ID: " + this.sellerID);
                return true;
            } else {
                System.out.println("Seller profile update did not affect any rows (ID: " + this.sellerID + "). Seller might not exist or data is unchanged.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error updating seller profile: " + e.getMessage());
            // e.printStackTrace();
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }
    
     
    // Update the seller type in the database (Free -> Gold)
    // newUserType The new user type
    // returns true if the update was successful, false otherwise
    
    public boolean updateUserTypeInDB(String newUserType) {
        if (this.sellerID <= 0) {
             System.err.println("Cannot update user type for a seller without a valid ID.");
             return false;
        }
        if (!"Free".equals(newUserType) && !"Gold".equals(newUserType)) {
            System.err.println("Invalid user type: " + newUserType + ". Must be 'Free' or 'Gold'.");
            return false;
        }

        String sql = "UPDATE Sellers SET UserType = ? WHERE SellerID = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnector.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newUserType);
            pstmt.setInt(2, this.sellerID);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Seller user type updated to '" + newUserType + "' for ID: " + this.sellerID);
                this.userType = newUserType; // Update object state as well
                return true;
            } else {
                System.out.println("Seller user type update did not affect any rows (ID: " + this.sellerID + ").");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error updating seller user type: " + e.getMessage());
            // e.printStackTrace();
            return false;
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (SQLException e) { /* ignore */ }
        }
    }

    // --- Getters --- (Setters below only modify object state, call DB update methods separately) ---

    public int getSellerID() { return sellerID; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    // No getter for password - security
    public Date getDateOfBirth() { return dateOfBirth; }
    public Boolean getConsentLocation() { return consentLocation; }
    public String getProfilePhoto() { return profilePhoto; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getUserType() { return userType; }
    public boolean isLoggedIn() { return loggedIn; }

    // --- Setters --- (These only change the object's state -> after call updateProfileInDB(), etc. ) ---

    // SellerID is set by DB, no public setter
    // UserType change is handled by updateUserTypeInDB

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setConsentLocation(Boolean consentLocation) { this.consentLocation = consentLocation; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    // Private setter for login status
    private void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }

     @Override
    public String toString() {
        return "Seller{" +
                "sellerID=" + sellerID +
                ", fullName='" + fullName + ''' +
                ", email='" + email + ''' +
                ", dateOfBirth=" + dateOfBirth +
                ", consentLocation=" + consentLocation +
                ", profilePhoto='" + (profilePhoto != null ? "[set]" : "[unset]") + ''' +
                ", phoneNumber='" + (phoneNumber != null ? phoneNumber : "[unset]") + ''' +
                ", userType='" + userType + ''' +
                ", loggedIn=" + loggedIn +
                '}' ";

    }

    // --- these methods moved to DAO classes ---
    /*
    public void listProperty(Property property) {
        System.out.println("[Refactored] Property listing logic should be in PropertyDAO/Service using SellerID: " + this.sellerID);
    }
    public void updateProperty(String propertyID) {
       System.out.println("[Refactored] Property update logic should be in PropertyDAO/Service.");
    }
    public boolean verifyProperty(String propertyID) {
       System.out.println("[Refactored] Property verification logic should be in PropertyVerificationDAO/Service.");
       return false;
    }
    */

    // Option: If we choose to implement more security features we would need to work with hashes
    // and implement equals() and hashCode() based on sellerID
}

