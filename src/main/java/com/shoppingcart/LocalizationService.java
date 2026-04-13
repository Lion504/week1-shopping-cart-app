package com.shoppingcart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocalizationService {

    private static final Logger LOGGER = Logger.getLogger(LocalizationService.class.getName());

    public Map<String, String> getLocalizationStrings(String language) {
        Map<String, String> localizationMap = new HashMap<>();
        String query = "SELECT `key`, value FROM localization_strings WHERE language = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, language);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet != null) {
                    while (resultSet.next()) {
                        String key = resultSet.getString("key");
                        String value = resultSet.getString("value");
                        localizationMap.put(key, value);
                    }
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e, () -> "Error fetching localization strings for language: " + language);
        }

        return localizationMap;
    }
}
