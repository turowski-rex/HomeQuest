package com.homequest;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentDAO {

    public Payment createPayment(Payment payment) {
        String sql = "INSERT INTO Payments (amount, paymentMethod) VALUES (?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setDouble(1, payment.getAmount());
            pstmt.setString(2, payment.getPaymentMethod());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                System.err.println("Creating payment failed, no rows affected.");
                return null;
            }

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys.next()) {
                    String generatedId = keys.getString(1);
                    payment.setPaymentID(generatedId);
                    return payment;
                } else {
                    System.err.println("Creating payment failed, no ID obtained.");
                    return null;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error creating payment: " + e.getMessage());
            return null;
        }
    }

    
    public Payment getPaymentById(String paymentID) {
        String sql = "SELECT paymentID, amount, paymentMethod FROM Payments WHERE paymentID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paymentID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Payment p = new Payment(
                        rs.getString("paymentID"),
                        rs.getDouble("amount"),
                        rs.getString("paymentMethod")
                    );
                    return p;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching payment by ID: " + e.getMessage());
        }
        return null;
    }

    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT paymentID, amount, paymentMethod FROM Payments";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                payments.add(new Payment(
                    rs.getString("paymentID"),
                    rs.getDouble("amount"),
                    rs.getString("paymentMethod")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all payments: " + e.getMessage());
        }
        return payments;
    }

  
    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE Payments SET amount = ?, paymentMethod = ? WHERE paymentID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, payment.getAmount());
            pstmt.setString(2, payment.getPaymentMethod());
            pstmt.setString(3, payment.getPaymentID());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating payment: " + e.getMessage());
            return false;
        }
    }


    public boolean deletePayment(String paymentID) {
        String sql = "DELETE FROM Payments WHERE paymentID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, paymentID);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting payment: " + e.getMessage());
            return false;
        }
    }
}
