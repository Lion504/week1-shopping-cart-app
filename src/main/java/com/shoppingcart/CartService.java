package com.shoppingcart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CartService {

    public int saveCartRecord(int totalItems, double totalCost, String language) {
        String query = "INSERT INTO cart_records (total_items, total_cost, language) VALUES (?, ?, ?)";
        int recordId = -1;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, totalItems);
            statement.setDouble(2, totalCost);
            statement.setString(3, language);
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                recordId = generatedKeys.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recordId;
    }

    public void saveCartItems(int cartRecordId, List<Double> prices, List<Integer> quantities, List<Double> subtotals) {
        String query = "INSERT INTO cart_items (cart_record_id, item_number, price, quantity, subtotal) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            for (int i = 0; i < prices.size(); i++) {
                statement.setInt(1, cartRecordId);
                statement.setInt(2, i + 1);
                statement.setDouble(3, prices.get(i));
                statement.setInt(4, quantities.get(i));
                statement.setDouble(5, subtotals.get(i));
                statement.addBatch();
            }

            statement.executeBatch();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveCart(int totalItems, double totalCost, String language, List<Double> prices, List<Integer> quantities, List<Double> subtotals) {
        int recordId = saveCartRecord(totalItems, totalCost, language);
        if (recordId != -1) {
            saveCartItems(recordId, prices, quantities, subtotals);
        }
    }
}
