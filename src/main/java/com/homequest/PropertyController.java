package com.homequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/properties")
public class PropertyController {

    private final PropertyDAO propertyDAO;

    //define the upload directory
    private static final String UPLOAD_DIR = "./uploads/property-images";

    @Autowired
    public PropertyController(PropertyDAO propertyDAO) {
        this.propertyDAO = propertyDAO;
         //upload directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));
        } catch (IOException e) {
            System.err.println("Could not create upload directory: " + UPLOAD_DIR);
            e.printStackTrace();
        }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
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

    //endpoint to upload photos for a property
    @PostMapping("/{propertyID}/photos")
    public ResponseEntity<String> uploadPropertyPhotos(@PathVariable int propertyID, @RequestParam("files") MultipartFile[] files) {
        if (files.length == 0) {
            return ResponseEntity.badRequest().body("No files received");
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    //generate a unique filename
                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = "";
                    if (originalFilename != null && originalFilename.contains(".")) {
                        fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }
                    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                    Path filePath = Paths.get(UPLOAD_DIR, uniqueFileName);

                    //save the file to the upload directory
                    Files.copy(file.getInputStream(), filePath);

                    //save the file path to the database
                    boolean dbSuccess = propertyDAO.addImagePath(propertyID, filePath.toString());

                    if (!dbSuccess) {
                        System.err.println("Failed to save image path to database for file: " + uniqueFileName);
                    }

                } catch (IOException e) {
                    System.err.println("Error uploading file: " + file.getOriginalFilename());
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file: " + file.getOriginalFilename());
                } catch (Exception e) {
                     System.err.println("An unexpected error occurred during file upload for: " + file.getOriginalFilename());
                     e.printStackTrace();
                     return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred during file upload: " + file.getOriginalFilename());
                }
            } else {
                System.out.println("Skipping empty file.");
            }
        }

        return ResponseEntity.ok("Files uploaded successfully");
    }
}