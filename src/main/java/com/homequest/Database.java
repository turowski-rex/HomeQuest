package com.homequest;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Property> listOfProperties;
    private List<Seller> listOfSellers;
    private List<Payment> listOfPayments;

    public Database() {
        this.listOfProperties = new ArrayList<>();
        this.listOfSellers = new ArrayList<>();
        this.listOfPayments = new ArrayList<>();
    }

    public boolean savaData(Object data) {
        if (data instanceof Property) {
            listOfProperties.add((Property) data);
            return true;
        } else if (data instanceof Seller) {
            listOfSellers.add((Seller) data);
            return true;
        } else if (data instanceof Payment) {
            listOfPayments.add((Payment) data);
            return true;
        }
        return false;
    }
    public boolean updateData(Object data) {
        if (data instanceof Property) {
            Property updatedProperty = (Property) data;
            for (int i = 0; i < listOfProperties.size(); i++) {
                Property existingProperty = listOfProperties.get(i);
                if (existingProperty.getPropertyID() == updatedProperty.getPropertyID()) {
                    listOfProperties.set(i, updatedProperty);
                    return true;
                }
            }
        } else if (data instanceof Seller) {
            Seller updatedSeller = (Seller) data;
            for (int i = 0; i < listOfSellers.size(); i++) {
                Seller existingSeller = listOfSellers.get(i);
                if (existingSeller.getSellerID() == updatedSeller.getSellerID()) {
                    listOfSellers.set(i, updatedSeller);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean deleteData(Object data) {
        if (data instanceof Property) {
            Property propertyToDelete = (Property) data;
            for (int i = 0; i < listOfProperties.size(); i++) {
                Property existingProperty = listOfProperties.get(i);
                if (existingProperty.getPropertyID() == propertyToDelete.getPropertyID()) {
                    listOfProperties.remove(i);
                    return true;
                }
            }
        } else if (data instanceof Seller) {
            Seller sellerToDelete = (Seller) data;
            for (int i = 0; i < listOfSellers.size(); i++) {
                Seller existingSeller = listOfSellers.get(i);
                if (existingSeller.getSellerID() == sellerToDelete.getSellerID()) {
                    listOfSellers.remove(i);
                    return true;
                }
            }
        } else if (data instanceof Payment) {
            Payment paymentToDelete = (Payment) data;
            for (int i = 0; i < listOfPayments.size(); i++) {
                Payment existingPayment = listOfPayments.get(i);
                if (existingPayment.getPaymentID().equals(paymentToDelete.getPaymentID())) {
                    listOfPayments.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean retrieveData(Object criteria) {
        if (criteria instanceof Integer) { 
            int propertyID = (Integer) criteria;
            for (Property property : listOfProperties) {
                if (property.getPropertyID() == propertyID) {
                    System.out.println("Property found: " + property.toString());
                    return true;
                }
            }
        } else if (criteria instanceof Property) { 
            Property criteriaProperty = (Property) criteria;
            for (Property property : listOfProperties) {
                if (property.getPropertyID() == criteriaProperty.getPropertyID()) {
                    System.out.println("Property found: " + property.toString());
                    return true;
                }
            }
        }
        return false;
    }

   
    public boolean verifyProperty(int propertyID) {
        for (Property property : listOfProperties) {
            if (property.getPropertyID() == propertyID) {
                return true;
            }
        }
        return false;
    }

    public boolean processPayment(String paymentID) {
        for (Payment payment : listOfPayments) {
            if (payment.getPaymentID().equals(paymentID)) {
                return payment.processPayment();
            }
        }
        return false;
    }

    // Getters and Setters
    public List<Property> getListOfProperties() {
        return listOfProperties;
    }

    public void setListOfProperties(List<Property> listOfProperties) {
        this.listOfProperties = listOfProperties;
    }

    public List<Seller> getListOfSellers() {
        return listOfSellers;
    }

    public void setListOfSellers(List<Seller> listOfSellers) {
        this.listOfSellers = listOfSellers;
    }

    public List<Payment> getListOfPayments() {
        return listOfPayments;
    }

    public void setListOfPayments(List<Payment> listOfPayments) {
        this.listOfPayments = listOfPayments;
    }
}
