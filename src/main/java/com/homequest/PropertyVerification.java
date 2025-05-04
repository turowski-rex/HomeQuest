package com.homequest;

public class PropertyVerification {

    private boolean verificationStatus;

    public PropertyVerification(boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public boolean verifyDocuments(String propertyID) {
        if (propertyID != null && !propertyID.trim().isEmpty()) {
            verificationStatus = true;
        } else {
            verificationStatus = false;
        }
        return verificationStatus;
    }
    

    // Getters and Setters

    public boolean isVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
}
