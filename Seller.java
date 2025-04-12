import java.time.LocalDate;

public class Seller {
    private int sellerId;
    private String fullName;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    private boolean consentLocation;
    private String profilePhoto;
    private String phoneNumber;
    private UserType userType;
    
    // Getter and setter
    public enum UserType {
        Free, Gold
    }
}