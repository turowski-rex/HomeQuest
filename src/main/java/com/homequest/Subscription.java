import java.util.Date;

public class Subscription {
    private String subscriptionType;
    private Date startDate;

  
    public Subscription() {
    
    }

    public Subscription(String subscriptionType, Date startDate) {
        this.subscriptionType = subscriptionType;
        this.startDate = startDate;
    }

    public boolean renewSubscription(Payment payment) {
        if (payment == null) {
            System.out.println("No payment provided. Cannot renew subscription.");
            return false;
        }
        if (payment.processPayment()) {
            this.startDate = new Date();
            System.out.println("Subscription renewed successfully. Start Date: " + startDate);
            return true;
        } else {
            System.out.println("Payment failed. Subscription renewal unsuccessful.");
            return false;
        }
    }

    // Getters and Setters
    public String getSubscriptionType() {
        return subscriptionType;
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType = subscriptionType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
