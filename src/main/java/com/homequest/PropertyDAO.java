package com.homequest;

import java.math.BigDecimal;

public class PropertyDAO {

    public void updateDetails(Property property, String newLocation, BigDecimal newPrice, Integer newSize, Integer newNumberOfRooms, PropertyType newType) {
        
        property.setLocation(newLocation);
        property.setPrice(newPrice);
        property.setSize(newSize);
        property.setNumberOfRooms(newNumberOfRooms);
        property.setPropertyType(newType);

        System.out.println("Updated Details for:" + property.getPropertyID());
    }
}
