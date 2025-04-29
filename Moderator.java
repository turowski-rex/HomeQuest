import java.util.List;

public class Moderator {

    
    private String moderatorID;
    private String moderatorKey;

    public Moderator() {

    }

    public Moderator(String moderatorID, String moderatorKey) {
        this.moderatorID = moderatorID;
        this.moderatorKey = moderatorKey;
    }

    
    public boolean upgrade(String sellerStatus) {
        if (sellerStatus == null || sellerStatus.isEmpty()) {
            System.out.println("Invalid seller status.");
            return false;
        }
        if (sellerStatus.equalsIgnoreCase("regular")) {
            System.out.println("Seller status upgraded to Gold.");
            return true;
        } else {
            System.out.println("Seller status is not eligible for an upgrade.");
            return false;
        }
    }

    public boolean takePayment(double subscriptionFees) {
        if (subscriptionFees <= 0) {
            System.out.println("Invalid subscription fee.");
            return false;
        }
        Payment payment = new Payment("MODERATOR", subscriptionFees, "ModeratorPayment");
        boolean result = payment.processPayment();
        if (result) {
            System.out.println("Subscription fee of $" + subscriptionFees + " processed successfully.");
        } else {
            System.out.println("Failed to process subscription fee of $" + subscriptionFees + ".");
        }
        return result;
    }

   
    public boolean sellerAuthentication(Object sellerInfo) {
        if (sellerInfo instanceof Seller) {
            Seller seller = (Seller) sellerInfo;
            System.out.println("Seller " + seller.getSellerID() + " authenticated successfully.");
            return true;
        }
        System.out.println("Seller authentication failed.");
        return false;
    }

    public boolean sellerVerification(Object sellerInfo) {
        if (sellerInfo instanceof Seller) {
            Seller seller = (Seller) sellerInfo;
            if (seller.getEmail() != null && seller.getEmail().contains("@")) {
                System.out.println("Seller " + seller.getSellerID() + " verified successfully.");
                return true;
            }
        }
        System.out.println("Seller verification failed.");
        return false;
    }

    public boolean propertyVerification(String propertyID) {
        if (propertyID == null || propertyID.trim().isEmpty()) {
            System.out.println("Invalid property ID.");
            return false;
        }
        PropertyVerification verifier = new PropertyVerification(true);
        boolean verified = verifier.verifyDocuments(propertyID);
        if (verified) {
            System.out.println("Property " + propertyID + " verified successfully.");
        } else {
            System.out.println("Property " + propertyID + " verification failed.");
        }
        return verified;
    }

    public boolean checkNumOfProperties(List<Property> propertyList) {
        if (propertyList == null) {
            System.out.println("Property list is null.");
            return false;
        }
        if (propertyList.size() > 0) {
            System.out.println("Property check passed. Total properties: " + propertyList.size());
            return true;
        } else {
            System.out.println("No properties found.");
            return false;
        }
    }

    // Getters and Setters

    public String getModeratorID() {
        return moderatorID;
    }

    public void setModeratorID(String moderatorID) {
        this.moderatorID = moderatorID;
    }

    public String getModeratorKey() {
        return moderatorKey;
    }

    public void setModeratorKey(String moderatorKey) {
        this.moderatorKey = moderatorKey;
    }
}
