package com.homequest;

import java.sql.Date; //for DateOfBirth
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Seller {

    //Sellers table columns
    private int sellerID; //primary Key, auto-incr.
    private String fullName;
    private String email;
    private String password; //plain text password - change to hash?
    private Date dateOfBirth;
    private Boolean consentLocation;
    private String profilePhoto; //Path
    private String phoneNumber;
    private String userType; //'Free' or 'Gold'
    private boolean loggedIn = false;

    public Seller() {}

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
    public Seller(@JsonProperty("fullName") String fullName, 
                    @JsonProperty("email") String email, 
                    @JsonProperty("password") String password, 
                    @JsonProperty("dateOfBirth") Date dateOfBirth, 
                    @JsonProperty("consentLocation") Boolean consentLocation, 
                    @JsonProperty("profilePhoto") String profilePhoto, 
                    @JsonProperty("phoneNumber") String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.password = password; // plain text password
        this.dateOfBirth = dateOfBirth;
        this.consentLocation = consentLocation;
        this.profilePhoto = profilePhoto;
        this.phoneNumber = phoneNumber;
        this.userType = "Free"; // Default to Free on registration
    }

    // -Getters-
    public int getSellerID() { return sellerID; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; } // Getter for password
    public Date getDateOfBirth() { return dateOfBirth; }
    public Boolean getConsentLocation() { return consentLocation; }
    public String getProfilePhoto() { return profilePhoto; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getUserType() { return userType; }
    public boolean isLoggedIn() { return loggedIn; }

    // -Setters-(These only change object state)

    // SellerID is set by DB, no public setter
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; } // Setter for password
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setConsentLocation(Boolean consentLocation) { this.consentLocation = consentLocation; }
    public void setProfilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setUserType(String userType) { this.userType = userType; } // Setter for user type
    // Private setter for login status
    public void setLoggedIn(boolean loggedIn) { this.loggedIn = loggedIn; }

    @Override
    public String toString() {
        return "Seller{" +
                "sellerID=" + sellerID +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", consentLocation=" + consentLocation +
                ", profilePhoto='" + (profilePhoto != null ? "[set]" : "[unset]") + '\'' +
                ", phoneNumber='" + (phoneNumber != null ? phoneNumber : "[unset]") + '\'' +
                ", userType='" + userType + '\'' +
                ", loggedIn=" + loggedIn +
                '}' ;
    }
}