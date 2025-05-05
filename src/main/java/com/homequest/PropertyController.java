package com.homequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<Property> getAllProperties() {
        //add getAllProperties to PropertyDAO
        return propertyDAO.getAllProperties();
    }
}