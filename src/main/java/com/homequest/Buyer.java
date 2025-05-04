package com.homequest;

import java.util.ArrayList;
import java.util.List;

public class Buyer {    
    private List<Property> searchHistory;
    private List<Property> linkedProperties;

    public Buyer() {
        this.searchHistory = new ArrayList<>();
        this.linkedProperties = new ArrayList<>();
    }

    
    public List<Property> searchProperty() {
        return searchHistory;
    }

    public Property viewDetails(int propertyID) {
        for (Property property : searchHistory) {
            if (property.getPropertyID() == propertyID) {
                return property;
            }
        }
        return null;
    }
    public void likeProperty(int propertyID) {
        for (Property property : searchHistory) {
            if (property.getPropertyID() == propertyID) {
                if (linkedProperties.contains(property)) {
                    System.out.println("Property with ID " + propertyID + " is already liked.");
                    return;
                }
                Review review = new Review();
                if (review.submitLike()) {  
                    linkedProperties.add(property);
                    System.out.println("Property with ID " + propertyID + " has been liked.");
                } else {
                    System.out.println("Failed to like property with ID " + propertyID + ".");
                }
                return;
            }
        }
        System.out.println("Property with ID " + propertyID + " not found.");
    }
    
    // Getters and Setters

    public List<Property> getSearchHistory() {
        return searchHistory;
    }

    public void setSearchHistory(List<Property> searchHistory) {
        this.searchHistory = searchHistory;
    }

    public List<Property> getLinkedProperties() {
        return linkedProperties;
    }

    public void setLinkedProperties(List<Property> linkedProperties) {
        this.linkedProperties = linkedProperties;
    }
}
