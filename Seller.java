import java.util.ArrayList;
import java.util.List;

public class Seller {

    private List<Property> propertyList;
    private String userID;
    private String password;
    private String email;
    private boolean loggedIn = false;

    
    public Seller(String userID, String password, String email) {
        this.userID = userID;
        this.password = password;
        this.email = email;
        this.propertyList = new ArrayList<>();
    }

    
    public void listProperty(Property property) {
        propertyList.add(property);
        System.out.println("Property with ID " + property.getPropertyID() + " has been listed.");
    }

   
    public void updateProperty(String propertyID) {
        boolean found = false;
        for (Property property : propertyList) {
            if (property.getPropertyID().equals(propertyID)) {
                System.out.println("Property with ID " + propertyID + " has been updated.");
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Property with ID " + propertyID + " not found.");
        }
    }

   
    public boolean verifyProperty(String propertyID) {
        for (Property property : propertyList) {
            if (property.getPropertyID().equals(propertyID)) {
                boolean verifiedStatus = property.verifyDocuments(propertyID);
                System.out.println("Property with ID " + propertyID + " verification status: " + verifiedStatus);
                return verifiedStatus;
            }
        }
        System.out.println("Property with ID " + propertyID + " not found.");
        return false;
    }

    public void register() {
        if (userID != null && !userID.isEmpty() &&
            password != null && !password.isEmpty() &&
            email != null && !email.isEmpty()) {
            System.out.println("Seller " + userID + " registered successfully!");
        } else {
            System.out.println("Registration failed for seller " + userID);
        }
    }

    public void login() {
        if (password != null && !password.isEmpty()) {
            loggedIn = true;
            System.out.println("Seller " + userID + " logged in.");
        } else {
            System.out.println("Login failed for seller " + userID + ". Invalid credentials.");
        }
    }

    public void logout() {
        if (loggedIn) {
            loggedIn = false;
            System.out.println("Seller " + userID + " has logged out.");
        } else {
            System.out.println("Seller " + userID + " is not logged in.");
        }
    }

    // Getters and Setters

    public List<Property> getPropertyList() {
        return propertyList;
    }

    public void setPropertyList(List<Property> propertyList) {
        this.propertyList = propertyList;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
