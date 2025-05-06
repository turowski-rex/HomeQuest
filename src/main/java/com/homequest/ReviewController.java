package com.homequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewDAO reviewDAO;

    @Autowired
    public ReviewController(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

   
    @GetMapping
    public List<Review> getAllReviews() {
        return reviewDAO.getAllReviews();
    }

    
    @GetMapping("/{reviewID}")
    public ResponseEntity<Review> getReviewById(@PathVariable int reviewID) {
        Review review = reviewDAO.getReviewById(reviewID);
        if (review != null) {
            return ResponseEntity.ok(review);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

   
    @PostMapping
    public ResponseEntity<Void> createReview(@RequestBody Review review) {
        int generatedId = reviewDAO.createReview(review);
        if (generatedId > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @PutMapping("/{reviewID}")
    public ResponseEntity<Void> updateReview(
            @PathVariable int reviewID,
            @RequestBody Review review
    ) {
        boolean updated = reviewDAO.updateReview(reviewID, review);
        if (updated) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    
    @DeleteMapping("/{reviewID}")
    public ResponseEntity<Void> deleteReview(@PathVariable int reviewID) {
        boolean deleted = reviewDAO.deleteReview(reviewID);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
