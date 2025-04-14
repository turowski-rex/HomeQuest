public class Property {
    private String propertyID;
    private String location;
    private double price;
    private double size;
    private int numRooms;
    private String type;
    private boolean verified;


    public Property() {

    }

    public Property(String propertyID, String location, double price, double size, int numRooms, String type, boolean verified) {
        this.propertyID = propertyID;
        this.location = location;
        this.price = price;
        this.size = size;
        this.numRooms = numRooms;
        this.type = type;
        this.verified = verified;
    }

   
    public void updateDetails(String newLocation, double newPrice, double newSize, int newNumRooms, String newType) {  
        this.location = newLocation;
        this.price = newPrice;
        this.size = newSize;
        this.numRooms = newNumRooms;
        this.type = newType;
        
        System.out.println("New details for property " + propertyID + ":");
        System.out.println("Location: " + location + ", Price: " + price + ", Size: " + size + ", Rooms: " + numRooms + ", Type: " + type);
    }

   
    public boolean verifyDocuments(String propertyID) {
        if (this.propertyID.equals(propertyID)) {
            this.verified = true;
            System.out.println("Property " + propertyID + " is verified.");
            return true;
        } else {
            System.out.println("Property ID mismatch. Verification failed.");
            return false;
        }
    }

    // Getters and Setters

    public String getPropertyID() {
        return propertyID;
    }

    public void setPropertyID(String propertyID) {
        this.propertyID = propertyID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
