package com.homequest;

public class Payment {

    private String paymentID;
    private double amount;
    private String paymentMethod;

   
    public Payment() {

    }
    public Payment(String paymentID, double amount, String paymentMethod) {
        this.paymentID = paymentID;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
    }

    public boolean processPayment() {
        if (paymentID != null && !paymentID.isEmpty() && amount > 0 && paymentMethod != null && !paymentMethod.isEmpty()) {
            System.out.println("Payment processed successfully for Payment ID: " + paymentID + ", Amount: $" + amount + ", Method: " + paymentMethod);
            return true;
        } else {
            System.out.println("Payment processing failed for Payment ID: " + paymentID);
            return false;
        }
    }

    // Getters and Setters

    public String getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
