package com.homequest;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ReviewDAO {

    
    public int createReview(Review review) {
        String sql = "INSERT INTO Reviews (liked, disliked, comment) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setBoolean(1, review.isLike());
            pstmt.setBoolean(2, review.isDislike());
            pstmt.setString(3, review.getComment());

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                System.err.println("Creating review failed, no rows affected.");
                return -1;
            }
            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating review: " + e.getMessage());
        }
        return -1;
    }

    
    public Review getReviewById(int reviewID) {
        String sql = "SELECT liked, disliked, comment FROM Reviews WHERE reviewID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reviewID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Review(
                        rs.getBoolean("liked"),
                        rs.getBoolean("disliked"),
                        rs.getString("comment")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching review by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Review> getAllReviews() {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT reviewID, liked, disliked, comment FROM Reviews";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Review r = new Review(
                    rs.getBoolean("liked"),
                    rs.getBoolean("disliked"),
                    rs.getString("comment")
                );
                list.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all reviews: " + e.getMessage());
        }
        return list;
    }

  
    public boolean updateReview(int reviewID, Review review) {
        String sql = "UPDATE Reviews SET liked = ?, disliked = ?, comment = ? WHERE reviewID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, review.isLike());
            pstmt.setBoolean(2, review.isDislike());
            pstmt.setString(3, review.getComment());
            pstmt.setInt(4, reviewID);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating review: " + e.getMessage());
            return false;
        }
    }

    
    public boolean deleteReview(int reviewID) {
        String sql = "DELETE FROM Reviews WHERE reviewID = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, reviewID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
            return false;
        }
    }
}
