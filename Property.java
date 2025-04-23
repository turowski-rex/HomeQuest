import java.math.BigDecimal; // For price representation
import java.util.List; // For potentially holding image paths later

// Enum matching the database ENUM constraint for PropertyType
enum PropertyType {
    APARTMENT("Apartment"),
    HOUSE("House"),
    CONDO("Condo"),
    LAND("Land");

    private final String dbValue;

    PropertyType(String dbValue) {
        this.dbValue = dbValue;
    }

    public String getDbValue() {
        return dbValue;
    }

    // Helper method to get Enum from database string value
    public static PropertyType fromDbValue(String value) {
        if (value == null) return null;
        for (PropertyType type : values()) {
            if (type.dbValue.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown PropertyType value: " + value);
        // Or return null / default value depending on error
    }
}

public class Property {

    // Fields of Properties table columns
    private int propertyID;        // INT PRIMARY KEY AUTO_INCREMENT
    private int sellerID;          // INT (Foreign Key)
    private String propertyName;   // VARCHAR(100) NOT NULL
    private String location;       // VARCHAR(255) NOT NULL
    private BigDecimal price;      // DECIMAL(10, 2) NOT NULL
    private Integer size;          // INT (use Integer wrapper for potential null)
    private Integer numberOfRooms; // INT (use Integer wrapper for potential null)
    private PropertyType propertyType; // ENUM('Apartment', 'House', 'Condo', 'Land')
    private Boolean isForRent;     // BOOLEAN
    private Integer rentDuration;  // INT (nullable, e.g., in months)
    private Boolean verificationStatus; // BOOLEAN (Matching the column name)

    // Transient field (not directly in Properties table, loaded from PropertyImages)
    // Could be populated by a DAO method after loading the Property object
    private List<String> imagePaths;

    // Constructor for loading data from Database (includes propertyID)
    public Property(int propertyID, int sellerID, String propertyName, String location, BigDecimal price,
                    Integer size, Integer numberOfRooms, PropertyType propertyType, Boolean isForRent,
                    Integer rentDuration, Boolean verificationStatus) {
        this.propertyID = propertyID;
        this.sellerID = sellerID;
        this.propertyName = propertyName;
        this.location = location;
        this.price = price;
        this.size = size;
        this.numberOfRooms = numberOfRooms;
        this.propertyType = propertyType;
        this.isForRent = isForRent;
        // Ensure rentDuration is null if not for rent
        this.rentDuration = (isForRent != null && isForRent) ? rentDuration : null;
        this.verificationStatus = verificationStatus;
    }

    // Constructor for creating a new Property before saving to DB (propertyID is auto-generated)
    public Property(int sellerID, String propertyName, String location, BigDecimal price,
                    Integer size, Integer numberOfRooms, PropertyType propertyType, Boolean isForRent,
                    Integer rentDuration, Boolean verificationStatus) {
         // propertyID will be set after insertion
        this.sellerID = sellerID;
        this.propertyName = propertyName;
        this.location = location;
        this.price = price;
        this.size = size;
        this.numberOfRooms = numberOfRooms;
        this.propertyType = propertyType;
        this.isForRent = isForRent;
        // Ensure rentDuration is null if not for rent
        this.rentDuration = (isForRent != null && isForRent) ? rentDuration : null;
        this.verificationStatus = verificationStatus; // Initial status, might be set by verification process
    }


    // --- Getters ---
    public int getPropertyID() { return propertyID; }
    public int getSellerID() { return sellerID; }
    public String getPropertyName() { return propertyName; }
    public String getLocation() { return location; }
    public BigDecimal getPrice() { return price; }
    public Integer getSize() { return size; }
    public Integer getNumberOfRooms() { return numberOfRooms; }
    public PropertyType getPropertyType() { return propertyType; }
    public Boolean getIsForRent() { return isForRent; }
    public Integer getRentDuration() { return rentDuration; }
    public Boolean getVerificationStatus() { return verificationStatus; }
    public List<String> getImagePaths() { return imagePaths; } // Getter for transient field

    // --- Setters --- (Mainly for creating/updating before DB interaction)

    // propertyID is set by the DB or constructor, no public setter needed.
    public void setSellerID(int sellerID) { this.sellerID = sellerID; } // Needed when creating new property
    public void setPropertyName(String propertyName) { this.propertyName = propertyName; }
    public void setLocation(String location) { this.location = location; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setSize(Integer size) { this.size = size; }
    public void setNumberOfRooms(Integer numberOfRooms) { this.numberOfRooms = numberOfRooms; }
    public void setPropertyType(PropertyType propertyType) { this.propertyType = propertyType; }
    public void setIsForRent(Boolean isForRent) {
         this.isForRent = isForRent;
         // Reset rent duration if set to not for rent
         if (isForRent == null || !isForRent) {
             this.rentDuration = null;
         }
    }
    public void setRentDuration(Integer rentDuration) {
        // Only allow setting duration if it's for rent
        if (this.isForRent != null && this.isForRent) {
             this.rentDuration = rentDuration;
        } else {
            this.rentDuration = null; // Ensure it's null if not for rent
            System.err.println("Warning: Cannot set rent duration unless property IsForRent is true.");
        }
    }
    public void setVerificationStatus(Boolean verificationStatus) { this.verificationStatus = verificationStatus; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; } // Setter for transient field

    // --- toString() method for basic representation ---
    @Override
    public String toString() {
        return "Property{" +
                "propertyID=" + propertyID +
                ", sellerID=" + sellerID +
                ", propertyName='" + propertyName + ''' +
                ", location='" + location + ''' +
                ", price=" + price +
                ", size=" + size +
                ", numberOfRooms=" + numberOfRooms +
                ", propertyType=" + (propertyType != null ? propertyType.name() : "null") +
                ", isForRent=" + isForRent +
                ", rentDuration=" + rentDuration +
                ", verificationStatus=" + verificationStatus +
                // ", imagePaths=" + (imagePaths != null ? imagePaths.size() + " images" : "none") +
                '}' ";
    }

    // --- From what I understand, these methods need to be moved to DAO (Data-Access-Object) classes ---
    /*
    public void updateDetails(String newLocation, double newPrice, double newSize, int newNumRooms, String newType) {
        // This logic now belongs in a DAO/Service class that takes a Property object
        // and generates a SQL UPDATE statement.
        System.out.println("[Refactored] Property update logic should be in PropertyDAO/Service.");
    }

    public boolean verifyDocuments(String propertyID) {
        // Verification involves updating PropertyVerification table and potentially
        // the verificationStatus field here. Belongs in PropertyVerificationDAO/Service.
        System.out.println("[Refactored] Property verification logic should be in PropertyVerificationDAO/Service.");
        return false;
    }
    */

    // Optional: Implement equals() and hashCode() here too, based on propertyID
}
