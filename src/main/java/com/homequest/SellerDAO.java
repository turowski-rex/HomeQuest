package com.homequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SellerDAO {

    private List<Property> propertyList = new ArrayList<>();

 
    public void listProperty(Property property) {
        propertyList.add(property);
        System.out.println("Property with ID " + property.getPropertyID() + " has been listed.");
    }

    public void updateProperty(int propertyID, String newPropertyName, String newLocation, BigDecimal newPrice, Integer newSize, Integer newNumberOfRooms,
                               PropertyType newType, Boolean newIsForRent, Integer newRentDuration, Boolean newVerificationStatus) {
        for (Property property : propertyList) {
            if (property.getPropertyID() == propertyID) {
                property.setPropertyName(newPropertyName);
                property.setLocation(newLocation);
                property.setPrice(newPrice);
                property.setSize(newSize);
                property.setNumberOfRooms(newNumberOfRooms);
                property.setPropertyType(newType);
                property.setIsForRent(newIsForRent);
                property.setRentDuration(newRentDuration);
                property.setVerificationStatus(newVerificationStatus);

                System.out.println("Property with ID " + propertyID + " has been updated.");
                return;
            }
        }
        System.out.println("Property with ID " + propertyID + " not found in DAO.");
    }


    public List<Property> getAllProperties() {
        return propertyList;
    }
}
