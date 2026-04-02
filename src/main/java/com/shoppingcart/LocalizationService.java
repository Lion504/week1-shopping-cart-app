package com.shoppingcart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class LocalizationService {

    public Map<String, String> getLocalizationStrings(String language) {
        Map<String, String> localizationMap = new HashMap<>();
        String query = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, language);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String key = resultSet.getString("key");
                String value = resultSet.getString("value");
                localizationMap.put(key, value);
            }

        } catch (SQLException e) {
            System.err.println("Error fetching localization strings: " + e.getMessage());
            e.printStackTrace();
        }

        return localizationMap;
    }
}
