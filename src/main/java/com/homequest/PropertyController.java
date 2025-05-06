package com.homequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyDAO propertyDAO;

    @Autowired
    public PropertyController(PropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
    }

    @GetMapping
    public List<Property> getProperties(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer minSize,
            @RequestParam(required = false) Integer maxSize,
            @RequestParam(required = false) Integer minRooms,
            @RequestParam(required = false) Integer maxRooms,
            @RequestParam(required = false) PropertyType propertyType,
            @RequestParam(required = false) Boolean isForRent,
            @RequestParam(required = false) Integer minRentDuration,
            @RequestParam(required = false) Integer maxRentDuration,
            @RequestParam(required = false) Boolean verificationStatus) {

        //using new getFilteredProperties method in PropertyDAO
        return propertyDAO.getFilteredProperties(
                location,
                minPrice,
                maxPrice,
                minSize,
                maxSize,
                minRooms,
                maxRooms,
                propertyType,
                isForRent,
                minRentDuration,
                maxRentDuration,
                verificationStatus);
    }

    @GetMapping("/{propertyID}")
    public ResponseEntity<Property> getPropertyById(@PathVariable int propertyID) {
        Property property = propertyDAO.getPropertyById(propertyID);
        if (property != null) {
            return ResponseEntity.ok(property);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property) {
        Property createdProperty = propertyDAO.createProperty(property);
        if (createdProperty != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); //error status
        }
    }

    @PutMapping("/{propertyID}")
    public ResponseEntity<Void> updateProperty(@PathVariable int propertyID, @RequestBody Property property) {
        boolean success = propertyDAO.updateProperty(propertyID, property);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{propertyID}")
    public ResponseEntity<Void> deleteProperty(@PathVariable int propertyID) {
        boolean success = propertyDAO.deleteProperty(propertyID);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}