public class Property {
    private int propertyId;
    private int sellerId;
    private String propertyName;
    private String location;
    private int price;
    private int size;
    private int numberOfRooms;
    private PropertyType propertyType;
    private boolean isForRent;
    private int rentDuration;
    private boolean isActive;
    
    // Getter and setter
    public enum PropertyType {
        Apartment, House, Condo, Land
    }
}