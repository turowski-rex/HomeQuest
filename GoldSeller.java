public class GoldSeller extends Seller {


    private String subscriptionType;

    public GoldSeller(String fullName, String email, String password, String subscriptionType) {
        super(fullName, email, password, null, null, null, null);
        this.subscriptionType = subscriptionType;
    }

 
    public void paySubscription(double fee) {
        Payment payment = new Payment("PAY1", fee, "Gold");
        if (payment.processPayment()) {
            System.out.println("Subscription fee of $" + fee + " paid successfully.");
        } else {
            System.out.println("Subscription payment failed.");
        }
    }
    

    // Getters and Setters

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }
}
