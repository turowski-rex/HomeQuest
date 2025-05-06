package com.homequest;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class SubscriptionDAO {

    
    public boolean createSubscription(Subscription subscription) {
        String sql = "INSERT INTO Subscriptions (subscriptionType, startDate) VALUES (?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, subscription.getSubscriptionType());
            pstmt.setTimestamp(2, new Timestamp(subscription.getStartDate().getTime()));

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error creating subscription: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a subscription by its type (primary key).
     * @param subscriptionType the type key
     * @return Subscription object or null if not found
     */
    public Subscription getSubscriptionByType(String subscriptionType) {
        String sql = "SELECT subscriptionType, startDate FROM Subscriptions WHERE subscriptionType = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, subscriptionType);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Date startDate = new Date(rs.getTimestamp("startDate").getTime());
                    return new Subscription(subscriptionType, startDate);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching subscription: " + e.getMessage());
        }
        return null;
    }

   
    public List<Subscription> getAllSubscriptions() {
        List<Subscription> list = new ArrayList<>();
        String sql = "SELECT subscriptionType, startDate FROM Subscriptions";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String type = rs.getString("subscriptionType");
                Date startDate = new Date(rs.getTimestamp("startDate").getTime());
                list.add(new Subscription(type, startDate));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching subscriptions: " + e.getMessage());
        }
        return list;
    }

  
    public boolean updateSubscriptionDate(String subscriptionType, Date newDate) {
        String sql = "UPDATE Subscriptions SET startDate = ? WHERE subscriptionType = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, new Timestamp(newDate.getTime()));
            pstmt.setString(2, subscriptionType);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating subscription: " + e.getMessage());
            return false;
        }
    }

    
    public boolean deleteSubscription(String subscriptionType) {
        String sql = "DELETE FROM Subscriptions WHERE subscriptionType = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, subscriptionType);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting subscription: " + e.getMessage());
            return false;
        }
    }
}