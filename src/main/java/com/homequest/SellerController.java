package com.homequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    private final SellerDAO sellerDAO;

    @Autowired
    public SellerController(SellerDAO sellerDAO) {
        this.sellerDAO = sellerDAO;
    }

    //endpoint for seller registration
    @PostMapping("/register")
    public ResponseEntity<Seller> registerSeller(@RequestBody Seller seller) {
        Seller createdSeller = sellerDAO.createSeller(seller);
        if (createdSeller != null) {
            //exclude password from the response - security
            createdSeller.setPassword(null);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSeller);
        } else {
            //assuming createSeller returns null on failure (duplicate email)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    //endpoint for seller login
    @PostMapping("/login")
    public ResponseEntity<Seller> loginSeller(@RequestBody Seller loginRequest) {
        //loginRequest = email and password field
        Seller seller = sellerDAO.getSellerByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (seller != null) {
            seller.setPassword(null);
            return ResponseEntity.ok(seller);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    //endpoint to get seller by ID
    @GetMapping("/{sellerID}")
    public ResponseEntity<Seller> getSellerById(@PathVariable int sellerID) {
        Seller seller = sellerDAO.getSellerById(sellerID);
        if (seller != null) {
             // Exclude password from the response for security
            seller.setPassword(null);
            return ResponseEntity.ok(seller);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //endpoint to update seller profile
    @PutMapping("/{sellerID}")
    public ResponseEntity<Void> updateSellerProfile(@PathVariable int sellerID, @RequestBody Seller seller) {
         //updates profile details but NOT email, password, or userType
         //Seller object in the request should contain the updated profile data
        boolean success = sellerDAO.updateSellerProfile(sellerID, seller);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //endpoint to update seller type to gold
     @PutMapping("/{sellerID}/userType")
    public ResponseEntity<Void> updateSellerUserType(@PathVariable int sellerID, @RequestBody String userType) {
        boolean success = sellerDAO.updateSellerUserType(sellerID, userType);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    //endpoint to delete seller
    @DeleteMapping("/{sellerID}")
    public ResponseEntity<Void> deleteSeller(@PathVariable int sellerID) {
        boolean success = sellerDAO.deleteSeller(sellerID);
        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
